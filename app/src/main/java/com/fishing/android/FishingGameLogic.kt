package com.fishing.android

import androidx.compose.runtime.Composable
import kotlin.random.Random
import kotlin.math.min
import kotlin.math.max

class FishingGameLogic(private val gameState: FishingGameState) {

//    fun castFishingRod(pressDuration: Long): Float {
//        // Longer press results in longer cast distance
//        val maxCastDuration = 3000L // 3 seconds max press
//        val normalizedDuration = min(pressDuration.toFloat() / maxCastDuration, 1f)
//        val castDistance = normalizedDuration * gameState.currentRod.castDistance
//
//        gameState.currentCastDistance = castDistance
//        return castDistance
//    }

//    fun attemptCatchFish(fishingSpot: FishingSpot): FishCatchResult {
//        val potentialFish = fishingSpot.availableFish
//        if (potentialFish.isEmpty()) {
//            return FishCatchResult.FAILURE // No fish in this spot
//        }
//        val selectedFish = potentialFish.random() // Select a random fish from the spot
//
//        // catch probability calculations
//        val lureEffectiveness = gameState.currentLure.effectiveness[selectedFish] ?: 0.1f
//        val rodStrength = gameState.currentRod.fishCatchMultiplier
//        val playerSkillMultiplier = 1 + (gameState.fishingSkillLevel * 0.2f)
//
//        val baseDifficulty = selectedFish.difficulty.ordinal * 2.0f
//        val randomMovementFactor = Random.nextFloat() * 0.5f + 2.1f
//        val fishDifficultyFactor = 0.4f / (1 + baseDifficulty * randomMovementFactor)
//        val baseCatchProbability = 2.0f * lureEffectiveness * rodStrength * playerSkillMultiplier * fishDifficultyFactor
//
//        val randomFactor = Random.nextFloat()
//
//        return if (randomFactor <= max(0.01f, min(baseCatchProbability, 0.95f))) {
//            gameState.catchFish(selectedFish)
////            gameState.currentFishPosition = null // Clear fish position after successful catch
//            FishCatchResult.SUCCESS(selectedFish)
//        } else {
//            FishCatchResult.FAILURE
//        }
//    }

    fun reelInFish(fish: FishType): ReelingResult {
        // reeling difficulty based on fish properties
        val fishSizeMultiplier = 1 + (fish.size * 0.5f)
        val fishWeightMultiplier = 1 + (fish.weight * 0.3f)

        // Higher difficulty fish fill the progress bar slower
        val fishResistance = (1 + fish.difficulty.ordinal * 3.0f) * fishSizeMultiplier * fishWeightMultiplier

        val rodReelStrength = gameState.currentRod.reelStrength
        val playerSkillLevel = gameState.fishingSkillLevel

        // Calculate reeling success probability
        val baseSuccessProbability = 0.5f
        val successFactor = (rodReelStrength * (1 + playerSkillLevel * 0.1f)) / (fishResistance + 0.1f)

        // Ensure probability is within [0, 1] range
        val reelSuccessProbability = max(0.03f, min(0.85f, baseSuccessProbability * successFactor))

        return if (Random.nextFloat() <= reelSuccessProbability) {
            ReelingResult.SUCCESS
        } else {
            ReelingResult.FAILURE
        }
    }
}

enum class ReelingResult {
    SUCCESS,
    FAILURE
}