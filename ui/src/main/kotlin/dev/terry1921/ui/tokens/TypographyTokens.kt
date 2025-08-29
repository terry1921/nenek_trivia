package dev.terry1921.ui.tokens

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Si usas Google Fonts, mapea aquí tu FontFamily
data class TypographyTokens(
    val display: TextStyle,
    val headline: TextStyle,
    val title: TextStyle,
    val body: TextStyle,
    val label: TextStyle
)

val DefaultTypographyTokens =
    TypographyTokens(
        display = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold, lineHeight = 44.sp),
        headline = TextStyle(
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 32.sp
        ),
        title = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold, lineHeight = 24.sp),
        body = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, lineHeight = 22.sp),
        label = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium, lineHeight = 16.sp)
    )

// Helper para mapear rápidamente a Material3 si lo necesitas
fun TypographyTokens.asMaterialTypography() = Typography(
    displayLarge = display,
    headlineLarge = headline,
    titleLarge = title,
    bodyLarge = body,
    labelLarge = label
)
