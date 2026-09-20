package com.chandra.expensetracker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object MainContainer : Screen("main_container") // Holds Bottom Nav
    object AddExpense : Screen("add_expense")
    object ChangePassword : Screen("change_password")
    object EditProfile : Screen("edit_profile")
    object Language : Screen("language")
    object AboutApp : Screen("about_app")
    object SoftwareUpdate : Screen("software_update")
    object TransactionHistory : Screen("transaction_history")
}

sealed class BottomBarScreen(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomBarScreen("home", "Home", Icons.Default.Home)
    object Settings : BottomBarScreen("settings", "Settings", Icons.Default.Settings)
    object Profile : BottomBarScreen("profile", "Profile", Icons.Default.Person)
}