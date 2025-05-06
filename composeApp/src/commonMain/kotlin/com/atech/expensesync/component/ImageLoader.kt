package com.atech.expensesync.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.SubcomposeAsyncImage

@Composable
fun LoadImageFromUrl(
    modifier: Modifier = Modifier,
    url: String
) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = "profile",
        modifier = modifier
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        error = {
            Icon(Icons.TwoTone.AccountCircle, null)
        }
    )
}