package com.fishing.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.fishing.android.FishingSpot
import com.fishing.android.R

@Composable
fun FishingLocationBackground(
    backgroundRes: Int? = null,
    fishingSpot: FishingSpot? = null,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val resId = backgroundRes ?: fishingSpotToDrawable(fishingSpot)
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
    }
}

// TODO need better images for each location
private fun fishingSpotToDrawable(spot: FishingSpot?): Int {
    return when (spot?.locationName) {
        R.string.location_beginners_pond -> R.drawable.sand_beach
        R.string.location_river_bend -> R.drawable.lake
        R.string.location_mountain_lake -> R.drawable.lake
        R.string.location_deep_river -> R.drawable.lake
        R.string.location_ocean_depths -> R.drawable.sand_beach
        R.string.location_mythical_waters -> R.drawable.sand_beach
        else -> R.drawable.sand_beach
    }
}