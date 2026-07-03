package com.fishing.android

// TODO need to update lure effectiveness, this is also unmaintainable if i add more fish
import androidx.annotation.StringRes
enum class LureType(
    @StringRes val displayName: Int,
    val effectiveness: Map<FishType, Float>
) {
    BASIC_WORM(R.string.lure_basic_worm_name, mapOf(
//        FishType.MINNOW to 0.8f,
        // TODO undo this when done testing.
        FishType.MINNOW to 0.0f,
        FishType.TROUT to 0.5f,
        FishType.BASS to 0.3f,
        FishType.SALMON to 0.2f,
        FishType.CATFISH to 0.1f,
        FishType.PIKE to 0.1f,
        FishType.STURGEON to 0.05f,
        FishType.MARLIN to 0.01f,
        FishType.TUNA to 0.01f,
        FishType.LEGENDARY_FISH to 0.001f
    )),
    ARTIFICIAL_FLY(R.string.lure_artificial_fly_name, mapOf(
        FishType.TROUT to 0.9f,
        FishType.SALMON to 0.7f,
        FishType.BASS to 0.5f,
        FishType.MINNOW to 0.4f,
        FishType.PIKE to 0.3f,
        FishType.CATFISH to 0.2f,
        FishType.STURGEON to 0.1f,
        FishType.MARLIN to 0.05f,
        FishType.TUNA to 0.05f,
        FishType.LEGENDARY_FISH to 0.01f
    )),
    SPINNER(R.string.lure_spinner_name, mapOf(
        FishType.PIKE to 0.8f,
        FishType.BASS to 0.7f,
        FishType.TROUT to 0.6f,
        FishType.SALMON to 0.5f,
        FishType.CATFISH to 0.4f,
        FishType.MINNOW to 0.3f,
        FishType.STURGEON to 0.2f,
        FishType.MARLIN to 0.1f,
        FishType.TUNA to 0.1f,
        FishType.LEGENDARY_FISH to 0.05f
    )),
    LIVE_BAIT(R.string.lure_live_bait_name, mapOf(
        FishType.CATFISH to 0.9f,
        FishType.BASS to 0.7f,
        FishType.MINNOW to 0.6f,
        FishType.TROUT to 0.5f,
        FishType.PIKE to 0.4f,
        FishType.SALMON to 0.3f,
        FishType.STURGEON to 0.2f,
        FishType.MARLIN to 0.1f,
        FishType.TUNA to 0.1f,
        FishType.LEGENDARY_FISH to 0.05f
    )),
    JERK_BAIT(R.string.lure_jerk_bait_name, mapOf(
        FishType.BASS to 0.8f,
        FishType.PIKE to 0.7f,
        FishType.TROUT to 0.6f,
        FishType.SALMON to 0.5f,
        FishType.CATFISH to 0.4f,
        FishType.STURGEON to 0.3f,
        FishType.MINNOW to 0.2f,
        FishType.MARLIN to 0.1f,
        FishType.TUNA to 0.1f,
        FishType.LEGENDARY_FISH to 0.05f
    )),
    SOFT_PLASTIC(R.string.lure_soft_plastic_name, mapOf(
        FishType.BASS to 0.9f,
        FishType.PIKE to 0.8f,
        FishType.CATFISH to 0.7f,
        FishType.TROUT to 0.6f,
        FishType.SALMON to 0.5f,
        FishType.STURGEON to 0.4f,
        FishType.MINNOW to 0.3f,
        FishType.MARLIN to 0.2f,
        FishType.TUNA to 0.2f,
        FishType.LEGENDARY_FISH to 0.1f
    )),
    DEEP_SEA_LURE(R.string.lure_deep_sea_lure_name, mapOf(
        FishType.MARLIN to 0.9f,
        FishType.TUNA to 0.8f,
        FishType.STURGEON to 0.7f,
        FishType.PIKE to 0.6f,
        FishType.BASS to 0.5f,
        FishType.SALMON to 0.4f,
        FishType.CATFISH to 0.3f,
        FishType.TROUT to 0.2f,
        FishType.MINNOW to 0.1f,
        FishType.LEGENDARY_FISH to 0.5f
    )),
    LEGENDARY_LURE(R.string.lure_legendary_lure_name, mapOf(
        FishType.LEGENDARY_FISH to 1.0f,
        FishType.MARLIN to 0.8f,
        FishType.TUNA to 0.7f,
        FishType.STURGEON to 0.6f,
        FishType.PIKE to 0.5f,
        FishType.BASS to 0.4f,
        FishType.SALMON to 0.3f,
        FishType.CATFISH to 0.2f,
        FishType.TROUT to 0.1f,
        FishType.MINNOW to 0.05f
    ))
}