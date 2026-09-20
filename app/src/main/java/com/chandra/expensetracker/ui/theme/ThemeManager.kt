package com.chandra.expensetracker.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontFamily

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

enum class ColorPaletteOption(val label: String, val primaryColorHex: Long) {
    PURPLE("Purple", 0xFF673AB7),
    EMERALD("Emerald Teal", 0xFF00897B),
    OCEAN("Ocean Blue", 0xFF1976D2),
    SUNSET("Sunset Orange", 0xFFE65100),
    CRIMSON("Crimson Rose", 0xFFC2185B)
}

enum class TextColorStyle(val label: String) {
    DEFAULT("Default"),
    HIGH_CONTRAST("High Contrast"),
    ACCENT_HIGHLIGHT("Accent Tint")
}

enum class FontFamilyOption(val label: String, val fontFamily: FontFamily) {
    SANS_SERIF("Sans-Serif", FontFamily.SansSerif),
    SERIF("Serif", FontFamily.Serif),
    MONOSPACE("Monospace", FontFamily.Monospace),
    CURSIVE("Cursive", FontFamily.Cursive)
}

enum class FontScaleOption(val label: String, val scale: Float) {
    SMALL("Small (90%)", 0.9f),
    NORMAL("Normal (100%)", 1.0f),
    LARGE("Large (110%)", 1.1f),
    EXTRA_LARGE("Extra Large (120%)", 1.2f)
}

object ThemeManager {

    var themeMode by mutableStateOf(ThemeMode.SYSTEM)
        private set

    var useDynamicColor by mutableStateOf(false)
        private set

    var colorPalette by mutableStateOf(ColorPaletteOption.PURPLE)
        private set

    var textColorStyle by mutableStateOf(TextColorStyle.DEFAULT)
        private set

    var fontFamilyOption by mutableStateOf(FontFamilyOption.SANS_SERIF)
        private set

    var fontScaleOption by mutableStateOf(FontScaleOption.NORMAL)
        private set

    // Backwards compatibility getter
    val isDarkTheme: Boolean
        @Composable
        get() = when (themeMode) {
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
        }

    private const val PREFS_NAME = "expense_tracker_prefs"
    private const val KEY_THEME_MODE = "theme_mode_enum"
    private const val KEY_DARK_MODE_LEGACY = "dark_mode"
    private const val KEY_DYNAMIC_COLOR = "dynamic_color"
    private const val KEY_COLOR_PALETTE = "color_palette"
    private const val KEY_TEXT_COLOR_STYLE = "text_color_style"
    private const val KEY_FONT_FAMILY = "font_family"
    private const val KEY_FONT_SCALE = "font_scale"

    fun load(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val legacyDark = prefs.getBoolean(KEY_DARK_MODE_LEGACY, false)
        val modeStr = prefs.getString(KEY_THEME_MODE, if (legacyDark) ThemeMode.DARK.name else ThemeMode.SYSTEM.name)
        themeMode = try {
            ThemeMode.valueOf(modeStr ?: ThemeMode.SYSTEM.name)
        } catch (_: Exception) {
            ThemeMode.SYSTEM
        }

        useDynamicColor = prefs.getBoolean(KEY_DYNAMIC_COLOR, false)

        val paletteStr = prefs.getString(KEY_COLOR_PALETTE, ColorPaletteOption.PURPLE.name)
        colorPalette = try {
            ColorPaletteOption.valueOf(paletteStr ?: ColorPaletteOption.PURPLE.name)
        } catch (_: Exception) {
            ColorPaletteOption.PURPLE
        }

        val textStyleStr = prefs.getString(KEY_TEXT_COLOR_STYLE, TextColorStyle.DEFAULT.name)
        textColorStyle = try {
            TextColorStyle.valueOf(textStyleStr ?: TextColorStyle.DEFAULT.name)
        } catch (_: Exception) {
            TextColorStyle.DEFAULT
        }

        val fontFamStr = prefs.getString(KEY_FONT_FAMILY, FontFamilyOption.SANS_SERIF.name)
        fontFamilyOption = try {
            FontFamilyOption.valueOf(fontFamStr ?: FontFamilyOption.SANS_SERIF.name)
        } catch (_: Exception) {
            FontFamilyOption.SANS_SERIF
        }

        val fontScaleStr = prefs.getString(KEY_FONT_SCALE, FontScaleOption.NORMAL.name)
        fontScaleOption = try {
            FontScaleOption.valueOf(fontScaleStr ?: FontScaleOption.NORMAL.name)
        } catch (_: Exception) {
            FontScaleOption.NORMAL
        }
    }

    fun setThemeMode(mode: ThemeMode, context: Context) {
        themeMode = mode
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_THEME_MODE, mode.name)
            .putBoolean(KEY_DARK_MODE_LEGACY, mode == ThemeMode.DARK)
            .apply()
    }

    fun setDarkTheme(enabled: Boolean, context: Context) {
        setThemeMode(if (enabled) ThemeMode.DARK else ThemeMode.LIGHT, context)
    }

    fun setDynamicColor(enabled: Boolean, context: Context) {
        useDynamicColor = enabled
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_DYNAMIC_COLOR, enabled).apply()
    }

    fun setColorPalette(palette: ColorPaletteOption, context: Context) {
        colorPalette = palette
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_COLOR_PALETTE, palette.name).apply()
    }

    fun setTextColorStyle(style: TextColorStyle, context: Context) {
        textColorStyle = style
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_TEXT_COLOR_STYLE, style.name).apply()
    }

    fun setFontFamily(option: FontFamilyOption, context: Context) {
        fontFamilyOption = option
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_FONT_FAMILY, option.name).apply()
    }

    fun setFontScale(option: FontScaleOption, context: Context) {
        fontScaleOption = option
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_FONT_SCALE, option.name).apply()
    }
}

