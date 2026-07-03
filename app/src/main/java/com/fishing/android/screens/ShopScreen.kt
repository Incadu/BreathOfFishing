package com.fishing.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fishing.android.FishingGameState
import com.fishing.android.FishingRodUtils
import com.fishing.android.R
import kotlinx.coroutines.delay

@Composable
fun ShopScreen(navController: NavController, gameState: FishingGameState) {
    var showUpgradeMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.shop_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Current Rod Info
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.current_rod),
                )
                Text(
                    text = stringResource(gameState.currentRod.displayNameResId),
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Rod Stats
                Text(text = "${stringResource(R.string.rod_cast_distance)}: ${gameState.currentRod.castDistance}")
                Text(text = "${stringResource(R.string.rod_reel_strength)}: ${gameState.currentRod.reelStrength}")
                Text(text = "${stringResource(R.string.rod_durability)}: ${gameState.currentRod.durability}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Next Rod Info and Upgrade Button
        val nextRod = FishingRodUtils.upgradeFishingRod(gameState.currentRod)
        if (nextRod != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.shop_next_rod),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(nextRod.displayNameResId),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Next Rod Stats
                    Text(text = "${stringResource(R.string.rod_cast_distance)}: ${nextRod.castDistance}")
                    Text(text = "${stringResource(R.string.rod_reel_strength)}: ${nextRod.reelStrength}")
                    Text(text = "${stringResource(R.string.rod_durability)}: ${nextRod.durability}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (gameState.upgradeRod()) {
                        showUpgradeMessage = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text(stringResource(R.string.shop_rods_button))
            }

            if (showUpgradeMessage) {
                LaunchedEffect(Unit) {
                    delay(2000)
                    showUpgradeMessage = false
                }
                Text(
                    text = stringResource(R.string.shop_rod_upgraded_text),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            Text(
                text = stringResource(R.string.shop_rod_max_upgrade_text),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("lure_shop_screen") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(stringResource(R.string.shop_lures_button))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back button
        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(stringResource(R.string.shop_exit_shop_button))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopScreenPreview() {
    val previewNavController = rememberNavController()
    ShopScreen(navController = previewNavController, gameState = FishingGameState())
}