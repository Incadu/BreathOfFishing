package com.fishing.android

import androidx.annotation.StringRes

// TODO should implement speed? for how fast the fish can move
enum class FishType(
    @StringRes val displayName: Int,
    val difficulty: FishDifficulty, 
    val size: Float, 
    val weight: Float
) {
    MINNOW(R.string.fish_minnow_name, FishDifficulty.VERY_EASY, 0.2f, 0.1f),
    TROUT(R.string.fish_trout_name, FishDifficulty.EASY, 0.5f, 0.5f),
    BASS(R.string.fish_bass_name, FishDifficulty.MEDIUM, 0.7f, 1.2f),
    SALMON(R.string.fish_salmon_name, FishDifficulty.MEDIUM_HARD, 0.8f, 2.0f),
    CATFISH(R.string.fish_catfish_name, FishDifficulty.HARD, 1.0f, 3.5f),
    PIKE(R.string.fish_pike_name, FishDifficulty.HARD, 1.2f, 4.0f),
    STURGEON(R.string.fish_sturgeon_name, FishDifficulty.VERY_HARD, 1.5f, 6.0f),
    MARLIN(R.string.fish_marlin_name, FishDifficulty.EXTREMELY_HARD, 2.0f, 8.5f),
    TUNA(R.string.fish_tuna_name, FishDifficulty.EXTREMELY_HARD, 1.8f, 7.5f),
    LEGENDARY_FISH(R.string.fish_legendary_fish_name, FishDifficulty.IMPOSSIBLE, 2.5f, 10.0f)
}

enum class FishDifficulty {
    VERY_EASY,
    EASY,
    MEDIUM,
    MEDIUM_HARD,
    HARD,
    VERY_HARD,
    EXTREMELY_HARD,
    IMPOSSIBLE
}