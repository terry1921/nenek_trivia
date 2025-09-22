package dev.terry1921.nenektrivia.ui.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
data class SizeTokens(
    val appBarHeightSize: Int = 56,
    val bottomBarHeightSize: Int = 56,

    val elevationXs: Int = 1,
    val elevationSm: Int = 2,
    val elevationMd: Int = 4,
    val elevationLg: Int = 8,
    val elevationXl: Int = 16,

    val buttonHeightSize: Int = 56,

    val iconButtonSizeSize: Int = 20,
    val iconMaxHeightSize: Int = 150,
    val iconMaxSize: Int = 150,

    val avatarSm: Int = 40,
    val avatarMd: Int = 60,
    val avatarLg: Int = 100,
    val avatarXl: Int = 150,
    val avatarXxl: Int = 200,

    val strokeWidthSm: Int = 1,
    val strokeWidthMd: Int = 2,
    val strokeWidthLg: Int = 4,

    val progressHeightSize: Int = 10,
    val circularProgressIndicatorSize: Int = 24,

    val paddingXs: Int = 4,
    val paddingSm: Int = 8,
    val paddingMd: Int = 12,
    val paddingLg: Int = 16,
    val paddingXl: Int = 24,
    val paddingXxl: Int = 32
) {
    val appBarHeight get() = appBarHeightSize.dp
    val bottomBarHeight get() = bottomBarHeightSize.dp

    val elevationExtraSmall get() = elevationXs.dp
    val elevationSmall get() = elevationSm.dp
    val elevationMedium get() = elevationMd.dp
    val elevationLarge get() = elevationLg.dp
    val elevationExtraLarge get() = elevationXl.dp

    val iconButtonSize get() = iconButtonSizeSize.dp
    val iconMaxHeight get() = iconMaxHeightSize.dp
    val iconMax get() = iconMaxSize.dp

    val buttonHeight get() = buttonHeightSize.dp

    val avatarSmall get() = avatarSm.dp
    val avatarMedium get() = avatarMd.dp
    val avatarLarge get() = avatarLg.dp
    val avatarExtraLarge get() = avatarXl.dp
    val avatarExtraExtraLarge get() = avatarXxl.dp

    val strokeWidthSmall get() = strokeWidthSm.dp
    val strokeWidthMedium get() = strokeWidthMd.dp
    val strokeWidthLarge get() = strokeWidthLg.dp

    val progressHeight get() = progressHeightSize.dp
    val circularProgressIndicator get() = circularProgressIndicatorSize.dp

    val paddingExtraSmall get() = paddingXs.dp
    val paddingSmall get() = paddingSm.dp
    val paddingMedium get() = paddingMd.dp
    val paddingLarge get() = paddingLg.dp
    val paddingExtraLarge get() = paddingXl.dp
    val paddingExtraExtraLarge get() = paddingXxl.dp
}

val DefaultSizes = SizeTokens()
