package com.atech.expensesync.ui.screens.scan.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.ui.theme.spacing
import kotlinx.coroutines.launch
import org.publicvalue.multiplatform.qrcode.CodeType
import org.publicvalue.multiplatform.qrcode.ScannerWithPermissions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    isScanning: Boolean,
    isScanningChanged: (Boolean) -> Unit,
    onNavigateUpClick: () -> Unit,
    onLinkScanned: (String) -> Unit,
) {
    var showHelp by remember { mutableStateOf(false) }

    MainContainer(
        modifier = modifier,
        title = "Scan QR Code",
        onNavigationClick = onNavigateUpClick,
        actions = {
            IconButton(onClick = { showHelp = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Help,
                    contentDescription = "Help"
                )
            }
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            val scope = rememberCoroutineScope()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.medium)
            ) {
                // Only show scanner when isScanning is true
                if (isScanning) {
                    ScannerWithPermissions(
                        modifier = Modifier.fillMaxSize(),
                        onScanned = { code ->
                            if (isScanning) {  // Check if we're still scanning
                                isScanningChanged(false)
                                scope.launch {
                                    onLinkScanned(code)
                                    onNavigateUpClick()
                                }
                            }
                            true  // Stop current scan
                        },
                        types = listOf(CodeType.QR),
                    )
                }

                // Rest of your UI components remain the same
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.Center)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    val infiniteTransition = rememberInfiniteTransition()
                    val scanLinePosition by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 250f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )

                    val color = MaterialTheme.colorScheme.primary
                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        drawLine(
                            color = color,
                            start = Offset(0f, scanLinePosition),
                            end = Offset(size.width, scanLinePosition),
                            strokeWidth = 3f
                        )
                    }
                }

                Text(
                    text = "Position QR code within the frame",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (showHelp) {
                AlertDialog(
                    onDismissRequest = { showHelp = false },
                    title = { Text("How to Scan QR Code") },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("1. Hold your device steady")
                            Text("2. Position the QR code within the frame")
                            Text("3. Ensure good lighting or use the flashlight")
                            Text("4. Wait for automatic scanning")
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showHelp = false }) {
                            Text("Got it")
                        }
                    }
                )
            }
        }
    }
}