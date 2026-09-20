package com.chandra.expensetracker

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class AppLanguage(val code: String, val englishName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    TELUGU("te", "Telugu", "తెలుగు"),
    KANNADA("kn", "Kannada", "కನ್ನಡ"),
    TAMIL("ta", "Tamil", "தமிழ்")
}

object LanguageManager {

    var currentLanguage by mutableStateOf(AppLanguage.ENGLISH)
        private set

    private const val PREFS_NAME = "expense_tracker_prefs"
    private const val KEY_LANGUAGE_CODE = "selected_language_code"

    fun load(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val code = prefs.getString(KEY_LANGUAGE_CODE, AppLanguage.ENGLISH.code)
        currentLanguage = AppLanguage.entries.find { it.code == code } ?: AppLanguage.ENGLISH
    }

    fun setLanguage(language: AppLanguage, context: Context) {
        currentLanguage = language
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE_CODE, language.code).apply()
    }
}
