package com.fishing.android.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fishing.android.FishingGameState
import com.fishing.android.FishingProcessState
import com.fishing.android.R
import com.fishing.android.ui.FishingLocationBackground
import com.fishing.android.vibrateDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.sqrt
import kotlin.random.Random

private fun Offset.magnitude(): Float = sqrt(x * x + y * y)
private fun Offset.normalize(): Offset {
    val mag = magnitude()
    return if (mag > 0) Offset(x / mag, y / mag) else Offset.Zero
}

@Composable
fun LuringScreen(
    navController: NavController,
    gameState: FishingGameState
) {
    // Track if a fish is interested in the lure
    var interestedFish = remember { mutableStateOf<FishData?>(null) }
    var fishBiting = remember { mutableStateOf(false) }

    // Calculate line end position based on cast distance
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val lineEndPosition = remember(gameState.lineEndPosition) {
        mutableStateOf(gameState.lineEndPosition)
    }

    // Reel handling
    val maxReelDistance = screenHeight * gameState.currentCastDistance
    var currentLineLength = remember { mutableFloatStateOf(maxReelDistance) }

    // biting handling
    var biteTimer = remember { mutableStateOf<Job?>(null) }
    // TODO could update this to be variable depending on fish difficulty, and rod stats.
    val biteTimeWindow = 2000L

    val startingPosition = remember {
        Offset(screenWidth / 2f, screenHeight - 120f)
    }

    @Composable
    fun ReelDonut(
        modifier: Modifier,
        onReelGesture: (Float, Boolean) -> Unit
    ) {
        var currentAngle = remember { mutableFloatStateOf(0f) }
        var lastAngle = remember { mutableFloatStateOf(0f) }

        Box(
            modifier = modifier
                .size(120.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val touchPoint = change.position
                        val angle = atan2(
                            touchPoint.y - center.y,
                            touchPoint.x - center.x
                        ) * (180f / PI.toFloat())

                        if (lastAngle.floatValue != 0f) {
                            var deltaAngle = angle - lastAngle.floatValue
                            if (deltaAngle > 180f) deltaAngle -= 360f
                            if (deltaAngle < -180f) deltaAngle += 360f
                            currentAngle.floatValue += deltaAngle

                            // Determine if rotation is clockwise
                            val isClockwise = deltaAngle < 0
                            onReelGesture(deltaAngle.absoluteValue, isClockwise)
                        }
                        lastAngle.floatValue = angle
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                // Draw outer circle
                drawCircle(
                    color = Color.Gray.copy(alpha = 0.3f),
                    radius = size.minDimension / 2
                )
                // Draw inner circle
                drawCircle(
                    color = Color.White,
                    radius = size.minDimension / 4
                )
            }
        }
    }

    fun handleReel(deltaAngle: Float, isClockwise: Boolean) {
        // Convert angle change to line movement
        val reelSpeed = 80f // TODO should better rods reels faster?
        val movement = (deltaAngle / 360f) * reelSpeed
        currentLineLength.floatValue = (currentLineLength.floatValue - movement).coerceIn(0f, maxReelDistance)

        val currentX = lineEndPosition.value.x
        val currentY = lineEndPosition.value.y

        val horizontalMovement = movement * 4f
        val verticalMovement = movement.absoluteValue * 2f

        val newX = if (isClockwise) {
            (currentX + horizontalMovement).coerceIn(0f, screenWidth)
        } else {
            (currentX - horizontalMovement).coerceIn(0f, screenWidth)
        }

        val newY = (currentY + verticalMovement).coerceIn(0f, screenHeight)
        lineEndPosition.value = Offset(newX, newY)

        gameState.lineEndPosition = lineEndPosition.value
        currentLineLength.floatValue = (screenHeight - lineEndPosition.value.y).coerceIn(0f, maxReelDistance)

        // Check if the line is below the bottom 1/4th of the screen
        if (lineEndPosition.value.y > screenHeight * 0.75f) {
            // Reset game state
            gameState.apply {
                currentFishingProcessState = FishingProcessState.IDLE
                hasCast = false
                linePosition = 0f
                lineEndPosition.value = startingPosition
            }
            // Navigate back to casting screen
            navController.navigate("casting_screen") {
                popUpTo("casting_screen") { inclusive = true }
            }
        }
    }

    // Animate existing fish
    LaunchedEffect(Unit) {
        while (true) {
            delay(16)
            val maxSpeed = 4f

            gameState.fishList.forEach { fish ->
                if (fish == interestedFish.value) {
                    // Move interested fish towards lure
                    val direction = (gameState.lineEndPosition - fish.position).normalize()
                    fish.position += direction * 5f
                    fish.isMovingRight = direction.x > 0

                    // Check if fish is close enough to bite
                    if ((fish.position - gameState.lineEndPosition).magnitude() < 50f && !fishBiting.value) {  // Increased detection radius
                        fishBiting.value = true
                        gameState.currentFishingProcessState = FishingProcessState.FISH_BITING

                        // Cancel existing timer if any
                        biteTimer.value?.cancel()

                        // Start new bite timer
                        biteTimer.value = CoroutineScope(Dispatchers.Main).launch {
                            delay(biteTimeWindow)
                            // Fish gets away if player didn't react in time
                            if (fishBiting.value) {
                                // Get the interested fish before clearing states
                                val fish = interestedFish.value
                                // Make fish swim away quickly
                                // TODO instead of teleporting, make it swim away in a random direction
                                fish?.let {
                                    val escapeDirection = (it.position - gameState.lineEndPosition).normalize()
                                    it.position += escapeDirection * 20000f // Large movement to escape
                                }
                                // Clear states
                                fishBiting.value = false
                                interestedFish.value = null
                                gameState.currentFishingProcessState = FishingProcessState.LINE_IN_WATER
                            }
                        }
                    }
                // Only check for new interest if no fish is currently interested
                } else if (interestedFish.value == null) {
                    // Normal fish movement
                    // TODO update this to make it more realistic, too spastic right now
                    val moveX = Random.nextFloat() * maxSpeed * 2 - maxSpeed
                    val moveY = Random.nextFloat() * maxSpeed * 2 - maxSpeed

                    val newX = (fish.position.x + moveX).coerceIn(0f, screenWidth - 40f)
                    val newY = (fish.position.y + moveY).coerceIn(0f, screenHeight * 0.5f - 40f)

                    fish.isMovingRight = newX > fish.position.x
                    fish.position = Offset(newX, newY)

                    // Check if fish gets interested in lure (increased detection radius and chance)
                    if ((fish.position - gameState.lineEndPosition).magnitude() < 150f &&
                        Random.nextFloat() < 0.05f  // Increased chance of interest
                    ) {
                        interestedFish.value = fish
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        FishingLocationBackground(fishingSpot = gameState.currentFishingSpot)

        // Draw fish
        gameState.fishList.forEach { fish ->
            Image(
                painter = painterResource(
                    id = R.drawable.fish_facing_right
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .offset { IntOffset(fish.position.x.toInt(), fish.position.y.toInt()) }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 160.dp),
            contentAlignment = Alignment.Center
        ) {
            ReelDonut(
                modifier = Modifier.padding(start = 16.dp),
                onReelGesture = { deltaAngle, isClockwise ->
                    handleReel(deltaAngle, isClockwise)
                }
            )
        }

        // Draw fishing line
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawLine(
                color = Color.Black,
                start = Offset(size.width / 2, size.height - 120f),
                end = lineEndPosition.value,
                strokeWidth = 2f
            )
        }

        // Draw fisherman
        Image(
            painter = painterResource(id = R.drawable.tiger_fisherman),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(120.dp)
                .offset(y = (-20).dp)
        )

        // Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    if (fishBiting.value) {
                        // Cancel any existing bite timer
                        biteTimer.value?.cancel()
                        biteTimer.value = null

                        // Get the interested fish
                        val fish = interestedFish.value

                        // TODO remove random chance of success
                        if (Random.nextFloat() < 0.7f) {
                            // Success - start reeling minigame
                            gameState.hookedFish = fish
                            gameState.selectedFishForReeling = gameState.currentFishingSpot
                                .availableFish.random()
                            gameState.currentFishingProcessState = FishingProcessState.REELING
                            navController.navigate("reeling_screen")
                        } else {
                            // Failed hook set - make fish swim away quickly
                            // TODO instead of teleporting, make it swim away in a random direction
                            fish?.let {
                                val escapeDirection =
                                    (it.position - gameState.lineEndPosition).normalize()
                                it.position += escapeDirection * 20000f // Large movement to escape
                            }
                            fishBiting.value = false
                            interestedFish.value = null
                            gameState.currentFishingProcessState = FishingProcessState.LINE_IN_WATER
                        }
                    }
                },
                enabled = fishBiting.value
            ) {
                val context = LocalContext.current
                if (fishBiting.value) {
                    vibrateDevice(context, longArrayOf(0, 150, 50, 300))
                }
                Text(if (fishBiting.value) {
                    stringResource(R.string.luring_set_hook_text)
                } else stringResource(R.string.luring_waiting_bite_text))
            }

            Button(
                onClick = {
                    gameState.currentFishingProcessState = FishingProcessState.IDLE
                    navController.popBackStack()
                }
            ) {
                Text(stringResource(R.string.luring_give_up_text))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LuringScreenPreview() {
    val previewNavController = rememberNavController()
    val previewFish = mutableListOf<FishData>().apply {
        add(FishData(id = 1, position = Offset(100f, 200f), isMovingRight = true))
        add(FishData(id = 2, position = Offset(300f, 250f), isMovingRight = false))
    }
    val previewGameState = FishingGameState().apply {
        fishList = previewFish
    }
    LuringScreen(
        navController = previewNavController,
        gameState = previewGameState
    )
}