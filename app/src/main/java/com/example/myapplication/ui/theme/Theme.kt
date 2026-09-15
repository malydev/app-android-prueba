package com.example.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BankColors = lightColorScheme(
    primary = Color(0xFF175D46), onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F1E5), onPrimaryContainer = Color(0xFF143D30),
    secondary = Color(0xFF6F795F), background = Color(0xFFF6F7F3),
    surface = Color(0xFFFFFFFF), onSurface = Color(0xFF182D26),
    onBackground = Color(0xFF182D26), onSurfaceVariant = Color(0xFF68776F),
    outline = Color(0xFFBBC7BF)
)
@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = BankColors, typography = Typography, content = content)
}
