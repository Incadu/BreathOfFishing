package com.fishing.android

enum class FishingRod(
    val displayNameResId: Int,
    val castDistance: Float,
    val reelStrength: Float,
    val durability: Int,
    val fishCatchMultiplier: Float,
    val reelZoneSize: Float
) {
    OAK_ROD(R.string.rod_oak_name, 9.0f, 0.85f, 100, 0.85f, 0.25f),
    PINE_ROD(R.string.rod_pine_name, 5.0f, 0.5f, 50, 0.6f, 0.15f),
    CEDAR_ROD(R.string.rod_cedar_name, 7.0f, 0.7f, 75, 0.75f, 0.20f),
    MAPLE_ROD(R.string.rod_maple_name, 11.0f, 0.95f, 150, 0.95f, 0.30f),
    IRONWOOD_ROD(R.string.rod_ironwood_name, 15.0f, 1.0f, 200, 1.0f, 0.35f)
}

// Companion object to provide helper methods for fishing rods
object FishingRodUtils {
    fun getStarterRod(): FishingRod = FishingRod.PINE_ROD
    
    fun upgradeFishingRod(currentRod: FishingRod): FishingRod? {
        return when (currentRod) {
            FishingRod.PINE_ROD -> FishingRod.CEDAR_ROD
            FishingRod.CEDAR_ROD -> FishingRod.OAK_ROD
            FishingRod.OAK_ROD -> FishingRod.MAPLE_ROD
            FishingRod.MAPLE_ROD -> FishingRod.IRONWOOD_ROD
            FishingRod.IRONWOOD_ROD -> null // Cannot upgrade further
        }
    }
}