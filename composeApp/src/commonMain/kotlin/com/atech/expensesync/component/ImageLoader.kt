package com.atech.expensesync.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.coil3.CoilImage
import expensesync.composeapp.generated.resources.Res
import expensesync.composeapp.generated.resources.profile
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoadImageFromUrl(
    modifier: Modifier = Modifier,
    url: String
) {
    CoilImage(
        modifier = modifier
            .clip(RoundedCornerShape(size = 50.dp)),
        imageModel = { url },
        previewPlaceholder = painterResource(Res.drawable.profile),
        loading = {
            CircularProgressIndicator()
        },
        failure = {
            Image(
                painter = painterResource(Res.drawable.profile),
                contentDescription = null
            )
        }
    )
}