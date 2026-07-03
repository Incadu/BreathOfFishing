package com.fishing.android


object FishingSpotGenerator {
    fun createDefaultSpot(): FishingSpot {
        return FishingSpot(
            availableFish = listOf(
                FishType.MINNOW,
                FishType.TROUT
            ),
            difficulty = FishDifficulty.VERY_EASY,
            locationName = R.string.location_beginners_pond
        )
    }

    fun generateFishingSpots(): List<FishingSpot> = listOf(
        FishingSpot(
            availableFish = listOf(
                FishType.MINNOW,
                FishType.TROUT
            ),
            difficulty = FishDifficulty.VERY_EASY,
            locationName = R.string.location_beginners_pond
        ),
        FishingSpot(
            availableFish = listOf(
                FishType.BASS,
                FishType.TROUT,
                FishType.SALMON
            ),
            difficulty = FishDifficulty.EASY,
            locationName = R.string.location_river_bend
        ),
        FishingSpot(
            availableFish = listOf(
                FishType.PIKE,
                FishType.CATFISH,
                FishType.BASS
            ),
            difficulty = FishDifficulty.MEDIUM,
            locationName = R.string.location_mountain_lake
        ),
        FishingSpot(
            availableFish = listOf(
                FishType.STURGEON,
                FishType.SALMON,
                FishType.PIKE
            ),
            difficulty = FishDifficulty.HARD,
            locationName = R.string.location_deep_river
        ),
        FishingSpot(
            availableFish = listOf(
                FishType.MARLIN,
                FishType.TUNA,
                FishType.STURGEON
            ),
            difficulty = FishDifficulty.EXTREMELY_HARD,
            locationName = R.string.location_ocean_depths
        ),
        FishingSpot(
            availableFish = listOf(
                FishType.LEGENDARY_FISH
            ),
            difficulty = FishDifficulty.IMPOSSIBLE,
            locationName = R.string.location_mythical_waters
        )
    )
}