package com.startapplab.stackexchange.ui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun ProfileAvatar(
    profileImage: String?,
    username: String,
    size: Dp = 48.dp,
    modifier: Modifier = Modifier
) {
    if (profileImage != null) {
        SubcomposeAsyncImage(
            model = profileImage,
            contentDescription = "Profile image of $username",
            modifier = modifier
                .size(size)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            loading = {
                SkeletonLoader(
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                )
            }
        )
    } else {
        Box(
            modifier = modifier
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = username.take(1).uppercase(),
                style = when {
                    size >= 100.dp -> MaterialTheme.typography.headlineLarge
                    size >= 60.dp -> MaterialTheme.typography.headlineMedium
                    else -> MaterialTheme.typography.titleMedium
                },
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
