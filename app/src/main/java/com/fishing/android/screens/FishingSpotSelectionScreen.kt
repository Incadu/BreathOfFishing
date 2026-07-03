package com.fishing.android.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fishing.android.FishingGameState
import com.fishing.android.FishingProcessState
import com.fishing.android.FishingSpot
import com.fishing.android.FishingSpotGenerator
import com.fishing.android.R

@Composable
fun FishingSpotSelectionScreen(
    navController: NavController,
    gameState: FishingGameState
) {
    val fishingSpots = remember { FishingSpotGenerator.generateFishingSpots() }

    // When a spot is selected, update the state and navigate
    fun onSpotSelected(spot: FishingSpot) {
        gameState.setFishingSpot(spot)
        navController.navigate("home_screen") {
            popUpTo("fishing_spot_selection") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.location_fishing_spot),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // TODO might be nice to have splash art for the spot background in the card
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(fishingSpots.size) { index ->
                val spot = fishingSpots[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onSpotSelected(spot) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            stringResource(spot.locationName),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "Difficulty: ${spot.difficulty}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            "Available Fish:",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        spot.availableFish.take(3).forEach { fish ->
                            Text(
                                "• ${stringResource(fish.displayName)}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        if (spot.availableFish.size > 3) {
                            Text(
                                "...and ${spot.availableFish.size - 3} more",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FishingSpotSelectionScreenPreview() {
    val previewNavController = rememberNavController()
    FishingSpotSelectionScreen(navController = previewNavController, gameState = FishingGameState())
}