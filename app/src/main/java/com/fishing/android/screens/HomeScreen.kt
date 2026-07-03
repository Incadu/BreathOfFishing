package com.fishing.android.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource // For string resources
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fishing.android.R
import androidx.navigation.compose.rememberNavController

object ScreenRoutes {
    const val SHOP_SCREEN = "shop_screen"
    const val CASTING_SCREEN = "casting_screen"
    const val HOME_SCREEN = "home_screen"
    const val LOCATION_SCREEN = "location_screen"
}

@Composable
fun HomeScreen(navController: NavController) {
    val commonButtonModifier = Modifier.fillMaxWidth()

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        // TODO add a better home screen background image
        Image(
            painter = painterResource(id = R.drawable.sand_beach),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // Home UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate(ScreenRoutes.CASTING_SCREEN) },
                modifier = commonButtonModifier
            ) {
                Text(text = stringResource(id = R.string.start_game_button))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(ScreenRoutes.SHOP_SCREEN) },
                modifier = commonButtonModifier
            ) {
                Text(text = stringResource(id = R.string.shop_button))
            }
            Button(
                onClick = { navController.navigate(ScreenRoutes.LOCATION_SCREEN) },
                modifier = commonButtonModifier
            ) {
                Text(text = stringResource(id = R.string.gameplay_change_location_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val previewNavController = rememberNavController()
    HomeScreen(navController = previewNavController)
}