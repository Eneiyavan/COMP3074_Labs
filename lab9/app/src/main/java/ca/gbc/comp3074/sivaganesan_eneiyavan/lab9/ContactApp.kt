package ca.gbc.comp3074.sivaganesan_eneiyavan.lab9

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.gbc.comp3074.sivaganesan_eneiyavan.lab9.ui.theme.ContactDetailsScreen
import ca.gbc.comp3074.sivaganesan_eneiyavan.lab9.ui.theme.ContactListScreen


@Composable
fun ContactApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "contacts"
    ) {
        composable("contacts") {
            ContactListScreen(
                onContactClick = { userId ->
                    navController.navigate("details/$userId")
                }
            )
        }

        composable(route = "details/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments
                ?.getString("userId")
                ?.toIntOrNull()

            ContactDetailsScreen(
                userId = userId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
