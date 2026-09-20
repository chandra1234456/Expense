package com.chandra.expensetracker.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Default Purple Light Theme Colors
val PrimaryLight = Color(0xFF673AB7)        // Deep Purple
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFEDE7F6)
val OnPrimaryContainerLight = Color(0xFF311B92)
val SecondaryLight = Color(0xFF3F51B5)      // Indigo
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFFE8EAF6)
val OnSecondaryContainerLight = Color(0xFF1A237E)
val BackgroundLight = Color(0xFFF8F9FA)      // Clean Off-White
val SurfaceLight = Color(0xFFFFFFFF)
val OnBackgroundLight = Color(0xFF1C1B1F)
val OnSurfaceLight = Color(0xFF1C1B1F)
val OutlineVariantLight = Color(0xFFE0E0E0)

// Default Purple Dark Theme Colors
val PrimaryDark = Color(0xFFB39DDB)         // Pastel Purple
val OnPrimaryDark = Color(0xFF311B92)
val PrimaryContainerDark = Color(0xFF512DA8)
val OnPrimaryContainerDark = Color(0xFFEDE7F6)
val SecondaryDark = Color(0xFF9FA8DA)       // Pastel Indigo
val OnSecondaryDark = Color(0xFF1A237E)
val SecondaryContainerDark = Color(0xFF303F9F)
val OnSecondaryContainerDark = Color(0xFFE8EAF6)
val BackgroundDark = Color(0xFF121212)       // AMOLED Black
val SurfaceDark = Color(0xFF1E1E1E)          // Charcoal Dark
val OnBackgroundDark = Color(0xFFE3E2E6)
val OnSurfaceDark = Color(0xFFE3E2E6)
val OutlineVariantDark = Color(0xFF333333)

fun getColorScheme(palette: ColorPaletteOption, darkTheme: Boolean, textColorStyle: TextColorStyle): ColorScheme {
    val (baseLight, baseDark) = when (palette) {
        ColorPaletteOption.PURPLE -> Pair(
            lightColorScheme(
                primary = PrimaryLight,
                onPrimary = OnPrimaryLight,
                primaryContainer = PrimaryContainerLight,
                onPrimaryContainer = OnPrimaryContainerLight,
                secondary = SecondaryLight,
                onSecondary = OnSecondaryLight,
                secondaryContainer = SecondaryContainerLight,
                onSecondaryContainer = OnSecondaryContainerLight,
                background = BackgroundLight,
                surface = SurfaceLight,
                onBackground = OnBackgroundLight,
                onSurface = OnSurfaceLight,
                outlineVariant = OutlineVariantLight
            ),
            darkColorScheme(
                primary = PrimaryDark,
                onPrimary = OnPrimaryDark,
                primaryContainer = PrimaryContainerDark,
                onPrimaryContainer = OnPrimaryContainerDark,
                secondary = SecondaryDark,
                onSecondary = OnSecondaryDark,
                secondaryContainer = SecondaryContainerDark,
                onSecondaryContainer = OnSecondaryContainerDark,
                background = BackgroundDark,
                surface = SurfaceDark,
                onBackground = OnBackgroundDark,
                onSurface = OnSurfaceDark,
                outlineVariant = OutlineVariantDark
            )
        )
        ColorPaletteOption.EMERALD -> Pair(
            lightColorScheme(
                primary = Color(0xFF00897B),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFE0F2F1),
                onPrimaryContainer = Color(0xFF004D40),
                secondary = Color(0xFF00ACC1),
                onSecondary = Color.White,
                secondaryContainer = Color(0xFFE0F7FA),
                onSecondaryContainer = Color(0xFF006064),
                background = Color(0xFFF4FBF7),
                surface = Color.White,
                onBackground = Color(0xFF191C1A),
                onSurface = Color(0xFF191C1A),
                outlineVariant = Color(0xFFD8E5DF)
            ),
            darkColorScheme(
                primary = Color(0xFF80CBC4),
                onPrimary = Color(0xFF003731),
                primaryContainer = Color(0xFF004D40),
                onPrimaryContainer = Color(0xFFE0F2F1),
                secondary = Color(0xFF80DEEA),
                onSecondary = Color(0xFF00363A),
                secondaryContainer = Color(0xFF004D40),
                onSecondaryContainer = Color(0xFFE0F7FA),
                background = Color(0xFF101412),
                surface = Color(0xFF1C221F),
                onBackground = Color(0xFFE1E3E0),
                onSurface = Color(0xFFE1E3E0),
                outlineVariant = Color(0xFF323B37)
            )
        )
        ColorPaletteOption.OCEAN -> Pair(
            lightColorScheme(
                primary = Color(0xFF1976D2),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFE3F2FD),
                onPrimaryContainer = Color(0xFF0D47A1),
                secondary = Color(0xFF0288D1),
                onSecondary = Color.White,
                secondaryContainer = Color(0xFFE1F5FE),
                onSecondaryContainer = Color(0xFF01579B),
                background = Color(0xFFF6F9FF),
                surface = Color.White,
                onBackground = Color(0xFF1A1C1E),
                onSurface = Color(0xFF1A1C1E),
                outlineVariant = Color(0xFFDCE2EC)
            ),
            darkColorScheme(
                primary = Color(0xFF90CAF9),
                onPrimary = Color(0xFF00325B),
                primaryContainer = Color(0xFF0D47A1),
                onPrimaryContainer = Color(0xFFE3F2FD),
                secondary = Color(0xFF81D4FA),
                onSecondary = Color(0xFF00344F),
                secondaryContainer = Color(0xFF01579B),
                onSecondaryContainer = Color(0xFFE1F5FE),
                background = Color(0xFF101317),
                surface = Color(0xFF1B1E24),
                onBackground = Color(0xFFE2E2E6),
                onSurface = Color(0xFFE2E2E6),
                outlineVariant = Color(0xFF343840)
            )
        )
        ColorPaletteOption.SUNSET -> Pair(
            lightColorScheme(
                primary = Color(0xFFE65100),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFFFE0B2),
                onPrimaryContainer = Color(0xFFE65100),
                secondary = Color(0xFFF57C00),
                onSecondary = Color.White,
                secondaryContainer = Color(0xFFFFE0B2),
                onSecondaryContainer = Color(0xFFBF360C),
                background = Color(0xFFFFFBF8),
                surface = Color.White,
                onBackground = Color(0xFF201A18),
                onSurface = Color(0xFF201A18),
                outlineVariant = Color(0xFFEBE0D8)
            ),
            darkColorScheme(
                primary = Color(0xFFFFB74D),
                onPrimary = Color(0xFF4A1A00),
                primaryContainer = Color(0xFF993300),
                onPrimaryContainer = Color(0xFFFFE0B2),
                secondary = Color(0xFFFFCC80),
                onSecondary = Color(0xFF421D00),
                secondaryContainer = Color(0xFF802D00),
                onSecondaryContainer = Color(0xFFFFE0B2),
                background = Color(0xFF171210),
                surface = Color(0xFF231C18),
                onBackground = Color(0xFFECE0DA),
                onSurface = Color(0xFFECE0DA),
                outlineVariant = Color(0xFF3F352E)
            )
        )
        ColorPaletteOption.CRIMSON -> Pair(
            lightColorScheme(
                primary = Color(0xFFC2185B),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFF8BBD0),
                onPrimaryContainer = Color(0xFF880E4F),
                secondary = Color(0xFFD81B60),
                onSecondary = Color.White,
                secondaryContainer = Color(0xFFF8BBD0),
                onSecondaryContainer = Color(0xFF4A0072),
                background = Color(0xFFFFF8F9),
                surface = Color.White,
                onBackground = Color(0xFF201A1C),
                onSurface = Color(0xFF201A1C),
                outlineVariant = Color(0xFFEAD8DF)
            ),
            darkColorScheme(
                primary = Color(0xFFF48FB1),
                onPrimary = Color(0xFF560027),
                primaryContainer = Color(0xFF880E4F),
                onPrimaryContainer = Color(0xFFF8BBD0),
                secondary = Color(0xFFF06292),
                onSecondary = Color(0xFF4A0023),
                secondaryContainer = Color(0xFF880E4F),
                onSecondaryContainer = Color(0xFFF8BBD0),
                background = Color(0xFF171013),
                surface = Color(0xFF231A1D),
                onBackground = Color(0xFFECE0E3),
                onSurface = Color(0xFFECE0E3),
                outlineVariant = Color(0xFF3E3337)
            )
        )
    }

    val selectedScheme = if (darkTheme) baseDark else baseLight

    return when (textColorStyle) {
        TextColorStyle.DEFAULT -> selectedScheme
        TextColorStyle.HIGH_CONTRAST -> {
            if (darkTheme) {
                selectedScheme.copy(
                    onBackground = Color(0xFFFFFFFF),
                    onSurface = Color(0xFFFFFFFF),
                    onSurfaceVariant = Color(0xFFF0F0F0)
                )
            } else {
                selectedScheme.copy(
                    onBackground = Color(0xFF000000),
                    onSurface = Color(0xFF000000),
                    onSurfaceVariant = Color(0xFF1C1C1C)
                )
            }
        }
        TextColorStyle.ACCENT_HIGHLIGHT -> {
            selectedScheme.copy(
                onBackground = if (darkTheme) selectedScheme.primary.copy(alpha = 0.95f) else selectedScheme.primaryContainer,
                onSurface = if (darkTheme) selectedScheme.primary else selectedScheme.primary
            )
        }
    }
}