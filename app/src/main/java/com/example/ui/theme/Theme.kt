package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = AcademicBlueVariant,
    secondary = AcademicSecondary,
    tertiary = AcademicSuccess,
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onPrimary = AcademicOnPrimary,
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = AcademicBlue,
    primaryContainer = AcademicPrimaryContainer,
    onPrimaryContainer = AcademicOnPrimaryContainer,
    secondary = AcademicSecondary,
    tertiary = AcademicSuccess,
    background = AcademicSurfaceBackground,
    surface = AcademicSurface,
    onPrimary = AcademicOnPrimary,
    onBackground = AcademicOnSurface,
    onSurface = AcademicOnSurface,
    onSurfaceVariant = AcademicOnSurfaceVariant,
    surfaceVariant = AcademicSurfaceVariant,
    outline = AcademicOutline,
    outlineVariant = AcademicOutlineVariant
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Disable dynamic color by default to strictly enforce Meskita brand colors
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
