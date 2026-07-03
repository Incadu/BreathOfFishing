import androidx.compose.runtime.*

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fishing.android.FishingGameState
import com.fishing.android.screens.CastingScreen
import com.fishing.android.screens.FishingSpotSelectionScreen
import com.fishing.android.screens.HomeScreen
import com.fishing.android.screens.LureShopScreen
import com.fishing.android.screens.LuringScreen
import com.fishing.android.screens.ShopScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val gameState = remember { FishingGameState() }

    NavHost(navController = navController, startDestination = "home_screen") {
        composable("home_screen") {
            HomeScreen(navController = navController)
        }
        composable("casting_screen") {
            CastingScreen(navController = navController, gameState = gameState)
        }
        composable("luring_screen") {
            LuringScreen(navController = navController, gameState = gameState)
        }
        composable("location_screen") {
            FishingSpotSelectionScreen(navController = navController, gameState = gameState)
        }
        composable("reeling_screen") {
            ReelingScreen(navController = navController, gameState = gameState)
        }
        composable("shop_screen") {
            ShopScreen(navController = navController, gameState = gameState)
        }
        composable("lure_shop_screen") {
            LureShopScreen(navController = navController, gameState = gameState)
        }
        composable("level_section_screen") {
            FishingSpotSelectionScreen(navController = navController, gameState = gameState)
        }

    }
}