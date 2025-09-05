package dev.terry1921.nenektrivia.ui.auth

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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.terry1921.nenektrivia.ui.R
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalShapeTokens
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

    val content = @Composable {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp), // más alto, estilo pill
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (loading) {
                CircularProgressIndicator(
                    color = color.circular,
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .size(20.dp)
                )
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
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
                contentColor = color.onPrimary
            ),
            content = { content() }
        )
        SocialButtonVariant.Outlined -> OutlinedButton(
            onClick = onClick,
            enabled = enabled && !loading,
            shape = shape,
            modifier = modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = color.onSurface
            ),
            border = ButtonDefaults.outlinedButtonBorder,
            content = { content() }
        )
    }
}

@Preview
@Composable
fun SocialLoginButtonPreview() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        SocialLoginButton(
            modifier = Modifier.padding(16.dp),
            label = "Continue with Google",
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
            modifier = Modifier.padding(16.dp),
            label = "Continue with Google",
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
            label = "Continue with Google",
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
            label = "Continue with Google",
            icon = ImageVector.vectorResource(id = R.drawable.ic_google),
            enabled = true,
            onClick = {},
            loading = true,
            variant = SocialButtonVariant.Outlined
        )
    }
}
