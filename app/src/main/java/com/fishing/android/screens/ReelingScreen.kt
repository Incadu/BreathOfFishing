import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fishing.android.FishType
import com.fishing.android.FishingGameLogic
import com.fishing.android.FishingGameState
import com.fishing.android.FishingProcessState
import com.fishing.android.FishingRodUtils
import com.fishing.android.FishingSpotGenerator
import com.fishing.android.LureType
import com.fishing.android.R
import kotlinx.coroutines.delay
import kotlin.random.Random
import androidx.compose.ui.platform.LocalDensity
import com.fishing.android.ui.FishingLocationBackground

@Composable
fun ReelingScreen(
    navController: NavController,
    gameState: FishingGameState
) {
    var fishPosition by remember { mutableFloatStateOf(0.5f) }
    var previousFishPosition by remember { mutableFloatStateOf(0.5f) }
    var isFishMovingRight by remember { mutableStateOf(true) }
    var touchXPosition by remember { mutableFloatStateOf(-1f) }
    var catchProgress by remember { mutableFloatStateOf(0.1f) }
    var gracePeriodActive by remember { mutableStateOf(true) }
    var graceTimeRemaining by remember { mutableFloatStateOf(2f) }
    val gracePeriodDuration = 2f

    var secondTouchActive by remember { mutableStateOf(false) }

    val playerCatchZoneWidthFactor by remember(gameState.currentRod) {
        derivedStateOf { gameState.currentRod.reelZoneSize.coerceIn(0.05f, 0.5f) }
    }

    var selectedFishForReeling = gameState.selectedFishForReeling
    val gameLogic = remember { FishingGameLogic(gameState) }

    FishingLocationBackground(fishingSpot = gameState.currentFishingSpot)

    LaunchedEffect(Unit) {
        while (gameState.currentFishingProcessState == FishingProcessState.REELING) {
            delay(150)
            previousFishPosition = fishPosition
            fishPosition = (fishPosition + Random.nextFloat() * 0.12f - 0.06f).coerceIn(0f, 1f)
            isFishMovingRight = fishPosition > previousFishPosition

            val zoneWidth = playerCatchZoneWidthFactor
            val playerZoneStart = (touchXPosition - zoneWidth / 2).coerceIn(0f, 1f)
            val playerZoneEnd = (touchXPosition + zoneWidth / 2).coerceIn(0f, 1f)
            val isFishInZone = fishPosition in playerZoneStart..playerZoneEnd

            if (isFishInZone) {
                catchProgress = (catchProgress + 0.05f + (gameState.currentRod.reelStrength * 0.005f)).coerceIn(0f, 1f)
            } else if (!gracePeriodActive) {
                val progressLoss = 0.03f + ((selectedFishForReeling?.weight ?: 0f) * 0.005f)
                catchProgress = (catchProgress - progressLoss).coerceIn(0f, 1f)
            }

            if (catchProgress >= 1f) {
                selectedFishForReeling?.let { fish ->
                    gameLogic.reelInFish(fish)
                    gameState.justCaughtFishMessage = navController.context.getString(R.string.reeling_success_text, fish.name)

                }
                gameState.fishList.remove(gameState.hookedFish)
                gameState.currentFishingProcessState = FishingProcessState.IDLE
                gameState.selectedFishForReeling = null
                gameState.hookedFish = null
                navController.navigate("casting_screen") {
                    popUpTo("casting_screen") { inclusive = true }
                }
                break
            } else if (catchProgress <= 0f) {
                gameState.justCaughtFishMessage = navController.context.getString(R.string.reeling_fish_escaped)
                gameState.fishList.remove(gameState.hookedFish)
                gameState.currentFishingProcessState = FishingProcessState.IDLE
                gameState.selectedFishForReeling = null
                navController.navigate("casting_screen") {
                    popUpTo("casting_screen") { inclusive = true }
                }
                break
            }
        }
    }

    LaunchedEffect(gracePeriodActive) {
        if (gracePeriodActive) {
            graceTimeRemaining = gracePeriodDuration
            while (graceTimeRemaining > 0 && gracePeriodActive) {
                delay(100)
                graceTimeRemaining = (graceTimeRemaining - 0.1f).coerceAtLeast(0f)
            }
            gracePeriodActive = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.reeling_fish_text),
            style = MaterialTheme.typography.headlineSmall)

        if (gracePeriodActive) {
            Text(
                text = "${stringResource(R.string.reeling_grace_title)}: ${"%.1f".format(graceTimeRemaining)}s",
                color = MaterialTheme.colorScheme.secondary)
        }

        Spacer(modifier = Modifier.height(8.dp))

        var trackWidthDp by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(60.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(horizontal = 4.dp)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val position = event.changes.firstOrNull()
                            if (position != null && position.pressed) {
                                touchXPosition = (position.position.x / size.width).coerceIn(0f, 1f)
                            }
                        }
                    }
                }
        ) {
            // ensure touch and sizes are within sensible bounds to avoid invalid Constraints
            val safeTouch = touchXPosition.takeIf { it in 0f..1f } ?: 0.5f
            val trackWidth = trackWidthDp.coerceAtLeast(0.dp)
            val playerIndicatorActualWidth = (trackWidth * playerCatchZoneWidthFactor).coerceIn(0.dp, trackWidth)
            val playerIndicatorOffset = (trackWidth - playerIndicatorActualWidth) * safeTouch
            val fishAvailableSpace = (trackWidth - 40.dp).coerceAtLeast(0.dp)

            // Draw catch zone following the players touch
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(playerIndicatorActualWidth)
                    .align(Alignment.CenterStart)
                    .offset(x = playerIndicatorOffset)
                     .background(
                         MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                         shape = MaterialTheme.shapes.small
                     )
            )

            // Draw fish
            // TODO replace with animated fish later for better effect
            Image(
                painter = painterResource(
                    id = if (isFishMovingRight) {
                        R.drawable.fish_facing_right
                    } else {
                        R.drawable.fish_facing_left
                    }
                ),
                contentDescription = "Fish",
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = fishAvailableSpace * fishPosition)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = { catchProgress },
             modifier = Modifier
                 .fillMaxWidth(0.9f)
                 .height(25.dp)
         )

        // Instructional text
        Text(
            stringResource(R.string.reeling_reel_instructional_text),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO need to make sure this is implemented correctly.
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.reeling_yank_instructional_text),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                if (secondTouchActive) {
                    Text(
                        stringResource(R.string.reeling_yank_text),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Button to cancel reeling
        Button(
            onClick = {
                gameState.currentFishingProcessState = FishingProcessState.IDLE
                gameState.justCaughtFishMessage = navController.context.getString(R.string.reeling_fail_text)
                gameState.selectedFishForReeling = null
                navController.popBackStack()
            }
        ) {
            Text(stringResource(R.string.reeling_give_up))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReelingScreenPreview() {
    MaterialTheme {
        val previewNavController = rememberNavController()
        ReelingScreen(
            navController = previewNavController, gameState = FishingGameState(
                currentRod = FishingRodUtils.getStarterRod(),
                currentLure = LureType.BASIC_WORM,
                caughtFish = mutableListOf(),
                currentCastDistance = 0f,
                currentFishingSpot = FishingSpotGenerator.createDefaultSpot(),
                playerExperience = 0,
                fishingSkillLevel = 1,
                currentFishingProcessState = FishingProcessState.REELING,
                selectedFishForReeling = FishType.TUNA,
                justCaughtFishMessage = stringResource(R.string.reeling_success_text, FishType.TUNA.name),
            )
        )
    }
}