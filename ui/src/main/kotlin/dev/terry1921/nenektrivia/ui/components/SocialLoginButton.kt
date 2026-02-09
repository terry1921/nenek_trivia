package dev.terry1921.nenektrivia.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.terry1921.nenektrivia.ui.R
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalShapeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSizeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSpacingTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalTypographyTokens
import dev.terry1921.nenektrivia.ui.tokens.asMaterialShapes

enum class SocialButtonVariant { Filled, Outlined }

@Composable
fun SocialLoginButton(
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit,
    loading: Boolean = false,
    variant: SocialButtonVariant = SocialButtonVariant.Filled,
    shape: Shape = LocalShapeTokens.current.asMaterialShapes().large
) {
    val typography = LocalTypographyTokens.current
    val color = LocalColorTokens.current
    val size = LocalSizeTokens.current
    val spacing = LocalSpacingTokens.current

    val content = @Composable {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = size.paddingLarge),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (loading) {
                CircularProgressIndicator(
                    color = color.circular,
                    strokeWidth = size.strokeWidthMedium,
                    modifier = Modifier
                        .size(size.circularProgressIndicator)
                )
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(size.iconButtonSize)
                )
            }
            Spacer(modifier = Modifier.size(spacing.medium))
            Text(text = label, style = typography.titleMedium)
        }
    }

    when (variant) {
        SocialButtonVariant.Filled -> Button(
            onClick = onClick,
            enabled = enabled && !loading,
            shape = shape,
            modifier = modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = color.primary,
                contentColor = color.whiteText
            ),
            content = { content() }
        )

        SocialButtonVariant.Outlined -> OutlinedButton(
            onClick = onClick,
            enabled = enabled && !loading,
            shape = shape,
            modifier = modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = color.darkText
            ),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true),
            content = { content() }
        )
    }
}

@Preview(
    name = "Social button",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun SocialLoginButtonPreview() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        SocialLoginButton(
            modifier = Modifier.padding(8.dp),
            label = stringResource(R.string.continue_with_google),
            icon = ImageVector.vectorResource(id = R.drawable.ic_google),
            enabled = true,
            onClick = {},
            loading = false,
            variant = SocialButtonVariant.Filled
        )
    }
}

@Preview(
    name = "Dark Social button",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SocialLoginButtonDarkPreview() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        SocialLoginButton(
            modifier = Modifier.padding(8.dp),
            label = stringResource(R.string.continue_with_google),
            icon = ImageVector.vectorResource(id = R.drawable.ic_google),
            enabled = true,
            onClick = {},
            loading = false,
            variant = SocialButtonVariant.Filled
        )
    }
}

@Preview
@Composable
fun SocialLoginButtonLoadingPreview() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        SocialLoginButton(
            modifier = Modifier.padding(8.dp),
            label = stringResource(R.string.continue_with_google),
            icon = ImageVector.vectorResource(id = R.drawable.ic_google),
            enabled = true,
            onClick = {},
            loading = true,
            variant = SocialButtonVariant.Filled
        )
    }
}

@Preview
@Composable
fun SocialLoginButtonPreviewOutlined() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        SocialLoginButton(
            modifier = Modifier.padding(16.dp),
            label = stringResource(R.string.continue_with_google),
            icon = ImageVector.vectorResource(id = R.drawable.ic_google),
            enabled = true,
            onClick = {},
            loading = false,
            variant = SocialButtonVariant.Outlined
        )
    }
}

@Preview
@Composable
fun SocialLoginButtonLoadingPreviewOutlined() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        SocialLoginButton(
            modifier = Modifier.padding(16.dp),
            label = stringResource(R.string.continue_with_google),
            icon = ImageVector.vectorResource(id = R.drawable.ic_google),
            enabled = true,
            onClick = {},
            loading = true,
            variant = SocialButtonVariant.Outlined
        )
    }
}
