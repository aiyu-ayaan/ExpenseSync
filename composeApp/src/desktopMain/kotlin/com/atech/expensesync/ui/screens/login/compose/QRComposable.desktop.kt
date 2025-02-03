package com.atech.expensesync.ui.screens.login.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.atech.expensesync.common.QRCodeImage

class QRHelperImp : QRHelper {
    @Composable
    override fun generateContent(): @Composable (() -> Unit) {
        return {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                , // Light Gray Background
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val qrCodeImage = QRCodeImage("https://www.youtube.com/watch?v=K4WvNAeKxbA&ab_channel=CaseyBonezSRT")

                    Image(
                        bitmap = qrCodeImage,
                        contentDescription = "QR Code",
                        modifier = Modifier
                            .size(250.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Scan the QR Code",
                        style = MaterialTheme.typography.h6,
                    )
                }
            }
        }
    }
}

@Composable
actual fun QRComposable(): QRHelper {
    return QRHelperImp()
}