package com.atech.expensesync.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo

@Composable
fun QRCodeImage(
    text: String,
    width: Int = 300,
    height: Int = 300
): ImageBitmap {
    val qrCodeWriter = QRCodeWriter()
    val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)

    // Create an empty byte buffer for pixel data
    val pixels = IntArray(width * height) { index ->
        val x = index % width
        val y = index / width
        if (bitMatrix.get(x, y)) Color.Black.toArgb() else Color.White.toArgb()
    }

    // Convert pixels to Skia ImageBitmap
    val imageInfo = ImageInfo(
        width = width,
        height = height,
        colorType = ColorType.RGBA_8888,
        alphaType = ColorAlphaType.PREMUL
    )

    return Image.makeRaster(
        imageInfo,
        pixels.flatMap { pixel ->
            listOf(
                (pixel shr 16 and 0xFF).toByte(), // Red
                (pixel shr 8 and 0xFF).toByte(),  // Green
                (pixel and 0xFF).toByte(),        // Blue
                (pixel shr 24 and 0xFF).toByte()  // Alpha
            )
        }.toByteArray(),
        width * 4
    ).toComposeImageBitmap()
}
