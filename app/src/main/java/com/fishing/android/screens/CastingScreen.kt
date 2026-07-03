package com.fishing.android.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fishing.android.FishingGameState
import com.fishing.android.FishingProcessState
import com.fishing.android.R
import com.fishing.android.ui.FishingLocationBackground
import kotlinx.coroutines.delay
import kotlin.math.sqrt
import kotlin.random.Random

data class FishData(
    val id: Int,
    var position: Offset,
    var isMovingRight: Boolean
)

@Composable
fun CastingScreen(
    navController: NavController,
    gameState: FishingGameState
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }

    var powerLevel by remember { mutableStateOf(0f) }
    var isPowerBarIncreasing by remember { mutableStateOf(true) }
    var isPressing by remember { mutableStateOf(false) }
    var hasCast by remember { mutableStateOf(false) }
    var touchPosition by remember { mutableStateOf(Offset.Zero) }
    var linePosition by remember { mutableStateOf(0f) }
    var messageVisible by remember { mutableStateOf(gameState.justCaughtFishMessage != null) }

    // This effect runs once to initialize the fish list and handle the message visibility
    LaunchedEffect(Unit) {
        if (gameState.fishList.isEmpty()) {
            gameState.fishList.addAll(
                List(Random.nextInt(5, 11)) { id ->
                    FishData(
                        id = id,
                        position = Offset(
                            x = Random.nextFloat() * screenWidth,
                            y = Random.nextFloat() * (screenHeight * 0.4f)
                        ),
                        isMovingRight = Random.nextBoolean()
                    )
                }
            )
        }

        if (messageVisible) {
            delay(3000)
            messageVisible = false
            gameState.justCaughtFishMessage = null // Clear the message after showing
        }
    }

    // Main animation loop for fish movement
    LaunchedEffect(Unit) {
        while (true) {
            // Animate fish movement
            gameState.fishList.forEach { fish ->
                val maxSpeed = 2f
                val moveX = Random.nextFloat() * maxSpeed * 2 - maxSpeed
                val moveY = Random.nextFloat() * maxSpeed * 2 - maxSpeed

                val newX = (fish.position.x + moveX).coerceIn(0f, screenWidth - 40f)
                val newY = (fish.position.y + moveY).coerceIn(0f, screenHeight * 0.5f - 40f)

                fish.isMovingRight = newX > fish.position.x
                fish.position = Offset(newX, newY)
            }
            delay(16)
        }
    }

    // Casting animation and power bar logic
    LaunchedEffect(hasCast, isPressing) {
        if (hasCast) {
            // Animate the line casting out after the user releases the press
            val targetDistance = screenHeight * powerLevel.coerceAtLeast(0.1f) // Ensure some distance
            val speed = 30f // Speed of the cast
            while (linePosition < targetDistance) {
                linePosition += speed
                delay(16)
            }
            // After cast is complete, update game state and navigate
            gameState.currentCastDistance = powerLevel
            gameState.linePosition = linePosition
            gameState.currentFishingProcessState = FishingProcessState.LINE_IN_WATER
            navController.navigate("luring_screen")

        } else if (isPressing) {
            // Animate power bar only when pressing
            while (isPressing && !hasCast) {
                if (isPowerBarIncreasing) {
                    powerLevel = (powerLevel + 0.02f).coerceAtMost(1f)
                    if (powerLevel >= 1f) isPowerBarIncreasing = false
                } else {
                    powerLevel = (powerLevel - 0.02f).coerceAtLeast(0f)
                    if (powerLevel <= 0f) isPowerBarIncreasing = true
                }
                delay(16)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FishingLocationBackground(fishingSpot = gameState.currentFishingSpot)

        // Fish Rendering
        gameState.fishList.forEach { fish ->
            val painter = if (fish.isMovingRight) {
                painterResource(R.drawable.fish_facing_right)
            } else {
                painterResource(R.drawable.fish_facing_left)
            }
            Image(
                painter = painter,
                contentDescription = "Fish",
                modifier = Modifier
                    .size(40.dp)
                    .offset { IntOffset(fish.position.x.toInt(), fish.position.y.toInt()) }
            )
        }

        // Fisherman Image
        Image(
            painter = painterResource(id = R.drawable.tiger_fisherman),
            contentDescription = "Fisherman",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(120.dp)
                .offset(y = (-20).dp)
        )

        // Fishing Line (visible after casting)
        if (hasCast) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val startPos = Offset(size.width / 2, size.height - 120f)
                val direction = (touchPosition - startPos).normalize()
                val endPos = startPos + (direction * linePosition)
                gameState.lineEndPosition = endPos
                drawLine(
                    color = Color.Black,
                    start = startPos,
                    end = endPos,
                    strokeWidth = 2f
                )
            }
        }

        // Power Bar (visible while pressing before cast)
        if (isPressing && !hasCast) {
            LinearProgressIndicator(
                progress = { powerLevel },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(20.dp)
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
                color = Color(red = (1 - powerLevel), green = powerLevel, blue = 0f)
            )
        }

        // Message after reeling in
        if (messageVisible && gameState.justCaughtFishMessage != null) {
            Text(
                text = gameState.justCaughtFishMessage!!,
                style = MaterialTheme.typography.headlineMedium,
                color = if (gameState.justCaughtFishMessage!!.contains("caught")) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 100.dp)
            )
        }

        // Home Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 32.dp, end = 16.dp)
        ) {
            IconButton(
                onClick = { navController.navigate(ScreenRoutes.HOME_SCREEN) },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(R.string.gameplay_home_button),
                    tint = Color.Black
                )
            }
        }

        // Clickable area for casting
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 64.dp, bottom = 80.dp) // Add padding to avoid home button area
                .pointerInput(hasCast) {
                    if (!hasCast) { // Only detect presses if the user hasn't cast yet
                        detectTapGestures(
                            onPress = { pressPosition ->
                                isPressing = true
                                touchPosition = pressPosition
                                try {
                                    awaitRelease()
                                } finally {
                                    isPressing = false
                                    hasCast = true
                                }
                            }
                        )
                    }
                }
        )
    }
}

private fun Offset.normalize(): Offset {
    val magnitude = magnitude()
    return if (magnitude > 0) this / magnitude else Offset(0f, -1f) // Default upward
}

private fun Offset.magnitude(): Float {
    return sqrt(x * x + y * y)
}

@Preview(showBackground = true)
@Composable
fun CastingScreenPreview() {
    val previewNavController = rememberNavController()
    val sampleGameState = FishingGameState().apply {
        justCaughtFishMessage = "You caught a big one!"
    }
    CastingScreen(navController = previewNavController, gameState = sampleGameState)
}
