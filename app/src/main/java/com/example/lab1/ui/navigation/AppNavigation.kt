package com.example.lab1.ui.navigation

import androidx.compose.runtime.*
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.*
import com.example.lab1.ui.screen.main.MainScreen
import com.example.lab1.ui.screen.main.MainViewModel
import com.example.lab1.ui.screen.timer.TimerScreen
import com.example.lab1.ui.screen.welcome.WelcomeScreen
import com.example.lab1.ui.screen.welcome.WelcomeViewModel

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Main : Screen("main")
    object Setup : Screen("setup")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {

        composable(Screen.Welcome.route) {
            val vm: WelcomeViewModel = hiltViewModel()
            val hasChar by vm.hasCharacter.collectAsState()

            LaunchedEffect(hasChar) {
                if (hasChar == true) {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            }

            if (hasChar == false) {
                WelcomeScreen(
                    vm = vm,
                    onNavigateToMain = {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Screen.Main.route) { backStackEntry ->
            val vm: MainViewModel = hiltViewModel(backStackEntry)

            MainScreen(
                vm = vm,
                onNavigateToSetup = {
                    navController.navigate(Screen.Setup.route)
                }
            )
        }

        composable(Screen.Setup.route) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Main.route)
            }

            val vm: MainViewModel = hiltViewModel(parentEntry)

            TimerScreen(
                vm = vm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}