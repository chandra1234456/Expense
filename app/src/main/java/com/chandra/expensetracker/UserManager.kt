package com.chandra.expensetracker

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit

object UserManager {

    var userName by mutableStateOf("Chandra Shekhar")
        private set

    var userEmail by mutableStateOf("chandra@example.com")
        private set

    var userPhone by mutableStateOf("+91 98765 43210")
        private set

    var isLoggedIn by mutableStateOf(true)
        private set

    private const val PREFS_NAME = "user_profile_prefs"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_PHONE = "user_phone"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    fun load(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        userName = prefs.getString(KEY_USER_NAME, "Chandra Shekhar") ?: "Chandra Shekhar"
        userEmail = prefs.getString(KEY_USER_EMAIL, "chandra@example.com") ?: "chandra@example.com"
        userPhone = prefs.getString(KEY_USER_PHONE, "+91 98765 43210") ?: "+91 98765 43210"
        isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, true)
    }

    fun updateProfile(name: String, email: String, phone: String, context: Context) {
        userName = name.ifBlank { "Chandra Shekhar" }
        userEmail = email.ifBlank { "chandra@example.com" }
        userPhone = phone.ifBlank { "+91 98765 43210" }

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(KEY_USER_NAME, userName)
                .putString(KEY_USER_EMAIL, userEmail)
                .putString(KEY_USER_PHONE, userPhone)
        }
    }

    fun logout(context: Context) {
        isLoggedIn = false
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_IS_LOGGED_IN, false) }
    }

    fun login(context: Context) {
        isLoggedIn = true
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_IS_LOGGED_IN, true) }
    }
}
