package dev.terry1921.nenektrivia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import dev.terry1921.nenektrivia.ui.R
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSizeTokens

@Composable
fun Avatar(url: String?, size: Dp, contentDescription: String?) {
    val color = LocalColorTokens.current
    var isLoading by remember { mutableStateOf(false) }

    if (url.isNullOrBlank()) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(color.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = contentDescription,
                modifier = Modifier.size(size * 0.5f),
                tint = color.onSurfaceVariant
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(color.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = url,
                contentDescription = contentDescription,
                placeholder = painterResource(id = R.drawable.ic_launcher),
                error = painterResource(id = R.drawable.ic_launcher),
                onLoading = { isLoading = true },
                onSuccess = { isLoading = false },
                onError = { isLoading = false },
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(size * 0.5f),
                    color = color.primary
                )
            }
        }
    }
}

@Preview
@Composable
fun AvatarPreview() {
    Avatar(
        url = null,
        size = LocalSizeTokens.current.avatarSize,
        contentDescription = "Avatar"
    )
}

@Preview
@Composable
fun AvatarPreviewUrl() {
    Avatar(
        url = "https://i.pravatar.cc/300",
        size = LocalSizeTokens.current.avatarSize,
        contentDescription = "Avatar"
    )
}
