package com.chandra.expensetracker.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ExpenseTrackerTheme(
    darkTheme: Boolean = when (ThemeManager.themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    },
    dynamicColor: Boolean = ThemeManager.useDynamicColor,
    palette: ColorPaletteOption = ThemeManager.colorPalette,
    textColorStyle: TextColorStyle = ThemeManager.textColorStyle,
    fontFamilyOption: FontFamilyOption = ThemeManager.fontFamilyOption,
    fontScaleOption: FontScaleOption = ThemeManager.fontScaleOption,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> getColorScheme(palette, darkTheme, textColorStyle)
    }

    val typography = getAppTypography(
        fontFamily = fontFamilyOption.fontFamily,
        scale = fontScaleOption.scale
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}