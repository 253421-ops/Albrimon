package com.example.albrimon.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.albrimon.presentation.auth.LoginScreen
import com.example.albrimon.presentation.auth.RegisterScreen
import com.example.albrimon.presentation.auth.WelcomeScreen
import com.example.albrimon.presentation.client.ClientDetailScreen
import com.example.albrimon.presentation.client.ClientListScreen
import com.example.albrimon.presentation.components.ComingSoonScreen
import com.example.albrimon.presentation.home.HomeScreen
import com.example.albrimon.presentation.project.ProjectListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME
    ) {
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onStart = { navController.navigate(Routes.REGISTER) },
                onLogin = { navController.navigate(Routes.LOGIN) }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                },
                onGoToLogin = { navController.navigate(Routes.LOGIN) }
            )
        }

        composable(Routes.HOME) { HomeScreen(navController = navController) }

        composable(Routes.CLIENTS) { ClientListScreen(navController = navController) }

        composable(
            route = Routes.CLIENT_DETAIL,
            arguments = listOf(navArgument("clientId") { type = NavType.IntType })
        ) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getInt("clientId") ?: 0
            ClientDetailScreen(clientId = clientId, navController = navController)
        }

        composable(Routes.PROJECTS) { ProjectListScreen(navController = navController) }

        composable(Routes.SCRAPS) {
            ComingSoonScreen(title = "Retazos", navController = navController)
        }

        composable(Routes.SOON) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: "Próximamente"
            ComingSoonScreen(title = title, navController = navController)
        }
    }
}
