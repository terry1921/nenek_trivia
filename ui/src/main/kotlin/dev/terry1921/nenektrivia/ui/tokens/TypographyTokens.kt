package dev.terry1921.nenektrivia.ui.tokens

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Si usas Google Fonts, mapea aquí tu FontFamily
data class TypographyTokens(
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val displaySmall: TextStyle,
    val headlineLarge: TextStyle,
    val headlineMedium: TextStyle,
    val headlineSmall: TextStyle,
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle
)

object TypeScaleTokens {
    // Display
    val DisplayLargeTracking = (-0.2).sp
    val DisplayLargeLineHeight = 44.sp
    val DisplayLargeSize = 40.sp
    val DisplayMediumTracking = 0.0.sp
    val DisplayMediumLineHeight = 40.0.sp
    val DisplayMediumSize = 36.sp
    val DisplaySmallTracking = 0.0.sp
    val DisplaySmallLineHeight = 36.0.sp
    val DisplaySmallSize = 32.sp

    // Headline
    val HeadlineLargeTracking = 0.0.sp
    val HeadlineLargeLineHeight = 36.0.sp
    val HeadlineLargeSize = 32.sp
    val HeadlineMediumTracking = 0.0.sp
    val HeadlineMediumLineHeight = 32.0.sp
    val HeadlineMediumSize = 28.sp
    val HeadlineSmallTracking = 0.0.sp
    val HeadlineSmallLineHeight = 28.0.sp
    val HeadlineSmallSize = 24.sp

    // Title
    val TitleLargeTracking = 0.0.sp
    val TitleLargeLineHeight = 28.0.sp
    val TitleLargeSize = 22.sp
    val TitleMediumTracking = 0.2.sp
    val TitleMediumLineHeight = 24.0.sp
    val TitleMediumSize = 16.sp
    val TitleSmallTracking = 0.1.sp
    val TitleSmallLineHeight = 20.0.sp
    val TitleSmallSize = 14.sp

    // Body
    val BodyLargeLineHeight = 24.0.sp
    val BodyLargeSize = 16.sp
    val BodyLargeTracking = 0.5.sp
    val BodyMediumLineHeight = 20.0.sp
    val BodyMediumSize = 14.sp
    val BodyMediumTracking = 0.2.sp
    val BodySmallLineHeight = 16.0.sp
    val BodySmallSize = 12.sp
    val BodySmallTracking = 0.4.sp

    // Label
    val LabelLargeTracking = 0.1.sp
    val LabelLargeLineHeight = 20.0.sp
    val LabelLargeSize = 14.sp
    val LabelMediumTracking = 0.5.sp
    val LabelMediumLineHeight = 16.0.sp
    val LabelMediumSize = 12.sp
    val LabelSmallTracking = 0.5.sp
    val LabelSmallLineHeight = 16.0.sp
    val LabelSmallSize = 11.sp
}

val DefaultTypographyTokens =
    TypographyTokens(
        displayLarge = TextStyle(
            fontSize = TypeScaleTokens.DisplayLargeSize,
            fontWeight = FontWeight.Bold,
            lineHeight = TypeScaleTokens.DisplayLargeLineHeight,
            letterSpacing = TypeScaleTokens.DisplayLargeTracking
        ),
        displayMedium = TextStyle(
            fontSize = TypeScaleTokens.DisplayMediumSize,
            fontWeight = FontWeight.Bold,
            lineHeight = TypeScaleTokens.DisplayMediumLineHeight,
            letterSpacing = TypeScaleTokens.DisplayMediumTracking
        ),
        displaySmall = TextStyle(
            fontSize = TypeScaleTokens.DisplaySmallSize,
            fontWeight = FontWeight.Bold,
            lineHeight = TypeScaleTokens.DisplaySmallLineHeight,
            letterSpacing = TypeScaleTokens.DisplaySmallTracking
        ),
        headlineLarge = TextStyle(
            fontSize = TypeScaleTokens.HeadlineLargeSize,
            fontWeight = FontWeight.SemiBold,
            lineHeight = TypeScaleTokens.HeadlineLargeLineHeight,
            letterSpacing = TypeScaleTokens.HeadlineLargeTracking
        ),
        headlineMedium = TextStyle(
            fontSize = TypeScaleTokens.HeadlineMediumSize,
            fontWeight = FontWeight.SemiBold,
            lineHeight = TypeScaleTokens.HeadlineMediumLineHeight,
            letterSpacing = TypeScaleTokens.HeadlineMediumTracking
        ),
        headlineSmall = TextStyle(
            fontSize = TypeScaleTokens.HeadlineSmallSize,
            fontWeight = FontWeight.SemiBold,
            lineHeight = TypeScaleTokens.HeadlineSmallLineHeight,
            letterSpacing = TypeScaleTokens.HeadlineSmallTracking
        ),
        titleLarge = TextStyle(
            fontSize = TypeScaleTokens.TitleLargeSize,
            fontWeight = FontWeight.SemiBold,
            lineHeight = TypeScaleTokens.TitleLargeLineHeight,
            letterSpacing = TypeScaleTokens.TitleLargeTracking
        ),
        titleMedium = TextStyle(
            fontSize = TypeScaleTokens.TitleMediumSize,
            fontWeight = FontWeight.SemiBold,
            lineHeight = TypeScaleTokens.TitleMediumLineHeight,
            letterSpacing = TypeScaleTokens.TitleMediumTracking
        ),
        titleSmall = TextStyle(
            fontSize = TypeScaleTokens.TitleSmallSize,
            fontWeight = FontWeight.SemiBold,
            lineHeight = TypeScaleTokens.TitleSmallLineHeight,
            letterSpacing = TypeScaleTokens.TitleSmallTracking
        ),
        bodyLarge = TextStyle(
            fontSize = TypeScaleTokens.BodyLargeSize,
            fontWeight = FontWeight.Normal,
            lineHeight = TypeScaleTokens.BodyLargeLineHeight,
            letterSpacing = TypeScaleTokens.BodyLargeTracking
        ),
        bodyMedium = TextStyle(
            fontSize = TypeScaleTokens.BodyMediumSize,
            fontWeight = FontWeight.Normal,
            lineHeight = TypeScaleTokens.BodyMediumLineHeight,
            letterSpacing = TypeScaleTokens.BodyMediumTracking
        ),
        bodySmall = TextStyle(
            fontSize = TypeScaleTokens.BodySmallSize,
            fontWeight = FontWeight.Normal,
            lineHeight = TypeScaleTokens.BodySmallLineHeight,
            letterSpacing = TypeScaleTokens.BodySmallTracking
        ),
        labelLarge = TextStyle(
            fontSize = TypeScaleTokens.LabelLargeSize,
            fontWeight = FontWeight.Medium,
            lineHeight = TypeScaleTokens.LabelLargeLineHeight,
            letterSpacing = TypeScaleTokens.LabelLargeTracking
        ),
        labelMedium = TextStyle(
            fontSize = TypeScaleTokens.LabelMediumSize,
            fontWeight = FontWeight.Medium,
            lineHeight = TypeScaleTokens.LabelMediumLineHeight,
            letterSpacing = TypeScaleTokens.LabelMediumTracking
        ),
        labelSmall = TextStyle(
            fontSize = TypeScaleTokens.LabelSmallSize,
            fontWeight = FontWeight.Medium,
            lineHeight = TypeScaleTokens.LabelSmallLineHeight,
            letterSpacing = TypeScaleTokens.LabelSmallTracking
        )
    )

// Helper para mapear rápidamente a Material3 si lo necesitas
fun TypographyTokens.asMaterialTypography() = Typography(
    displayLarge = displayLarge,
    displayMedium = displayMedium,
    displaySmall = displaySmall,
    headlineLarge = headlineLarge,
    headlineMedium = headlineMedium,
    headlineSmall = headlineSmall,
    titleLarge = titleLarge,
    bodyLarge = bodyLarge,
    labelLarge = labelLarge
)
