package com.fishing.android

data class FishingSpot(
    val availableFish: List<FishType>,
    val difficulty: FishDifficulty,
    val locationName: Int
)
