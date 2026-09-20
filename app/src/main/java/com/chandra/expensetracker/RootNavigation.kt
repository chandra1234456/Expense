package com.chandra.expensetracker

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chandra.expensetracker.login.LoginScreen

@Composable
fun RootNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        // Default slide transitions for standard flow
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(400)
            ) + fadeOut(animationSpec = tween(400))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(400)
            ) + fadeOut(animationSpec = tween(400))
        }
    ) {
        composable(
            route = Screen.Splash.route,
            // Splash screen should fade in/out cleanly
            enterTransition = { EnterTransition.None },
            exitTransition = { fadeOut(animationSpec = tween(600)) }
        ) {
            SplashScreen(onAnimationFinished = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        
        composable(
            route = Screen.Login.route,
            // When coming from splash, fade in, otherwise use default horizontal slide
            enterTransition = {
                if (initialState.destination.route == Screen.Splash.route) {
                    fadeIn(animationSpec = tween(600))
                } else {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(400)
                    ) + fadeIn(animationSpec = tween(400))
                }
            }
        ) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.MainContainer.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignup = { navController.navigate(Screen.Signup.route) }
            )
        }
        
        composable(route = Screen.Signup.route) {
            SignupScreen(onNavigateToLogin = { navController.navigate(Screen.Login.route) })
        }
        
        composable(route = Screen.MainContainer.route) {
            MainContainerScreen(rootNavController = navController)
        }
        
        composable(
            route = Screen.AddExpense.route,
            // Add Expense slides up modally from bottom
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(450)
                ) + fadeIn(animationSpec = tween(450))
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(450)
                ) + fadeOut(animationSpec = tween(450))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(450))
            },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(450)
                ) + fadeOut(animationSpec = tween(450))
            }
        ) {
            AddExpenseScreen(onBack = { navController.popBackStack() })
        }

        composable(route = Screen.ChangePassword.route) {
            ChangePasswordScreen(onBackClick = { navController.popBackStack() })
        }

        composable(route = Screen.EditProfile.route) {
            EditProfileScreen(onBackClick = { navController.popBackStack() })
        }

        composable(route = Screen.Language.route) {
            LanguageScreen(onBackClick = { navController.popBackStack() })
        }

        composable(route = Screen.AboutApp.route) {
            AboutAppScreen(
                onBackClick = { navController.popBackStack() },
                onCheckForUpdatesClick = { navController.navigate(Screen.SoftwareUpdate.route) }
            )
        }

        composable(route = Screen.SoftwareUpdate.route) {
            SoftwareUpdateScreen(onBackClick = { navController.popBackStack() })
        }

        composable(route = Screen.TransactionHistory.route) {
            TransactionHistoryScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
