package com.fishing.android

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import com.fishing.android.screens.FishData
import androidx.compose.ui.geometry.Offset

enum class FishingProcessState {
    IDLE,               // Default state, waiting for player to cast
    CASTING,            // Player is casting the line
    LINE_IN_WATER,      // Line has been cast, waiting for a bite (luring waiting)
    FISH_BITING,        // Fish is on the hook, player needs to react
    REELING,            // Player is actively reeling in the fish
}

data class FishingGameState(
    var currentRod: FishingRod = FishingRodUtils.getStarterRod(),
    var currentLure: LureType = LureType.BASIC_WORM,
    var caughtFish: MutableList<FishType> = mutableListOf(),
    var currentCastDistance: Float = 0f,
    var currentFishingSpot: FishingSpot = FishingSpotGenerator.createDefaultSpot(),
    var playerExperience: Int = 0,
    var fishingSkillLevel: Int = 1,
    var currentFishingProcessState: FishingProcessState = FishingProcessState.IDLE,
    var selectedFishForReeling: FishType? = null,
    var justCaughtFishMessage: String? = null,
    var fishList: MutableList<FishData> = mutableStateListOf(),
    var lineEndPosition: Offset = Offset.Zero,
    var powerLevel: Float = 0f,
    var hasCast: Boolean = false,
    var hookedFish: FishData? = null,
    var linePosition: Float = 0f,
    var touchPosition: Any = Offset.Zero
) {
    fun setFishingSpot(spot: FishingSpot) {
        currentFishingSpot = spot
        currentFishingProcessState = FishingProcessState.IDLE
}

    fun upgradeRod(): Boolean {
        val upgradedRod = FishingRodUtils.upgradeFishingRod(currentRod)
        return if (upgradedRod != null) {
            currentRod = upgradedRod
            true
        } else {
            false
        }
    }
}