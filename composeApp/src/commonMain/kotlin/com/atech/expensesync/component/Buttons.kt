package com.atech.expensesync.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.atech.expensesync.ui.theme.spacing
import expensesync.composeapp.generated.resources.Res
import expensesync.composeapp.generated.resources.ic_google_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    text: String,
    enable: Boolean = true,
    innerPadding: Dp = 0.dp,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = colors,
        enabled = enable
    ) {
        Text(
            text,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ButtonWithBorder(
    modifier: Modifier = Modifier,
    text: String,
    enable: Boolean = true,
    innerPadding: Dp = 0.dp,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    borderColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = .3f),
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        colors = colors,
        enabled = enable,
        border = BorderStroke(width = 1.dp, color = borderColor)
    ) {
        Text(
            text,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * Google button
 * Custom google button with google logo
 * @param modifier Modifier
 * @param text String text to display
 * @param loadingText String loading text to display
 * @param icon ImageVector? icon
 * @param shape Shape
 * @param borderColor Color
 * @param backgroundColor Color
 * @param progressIndicatorColor Color
 * @param hasClick Boolean
 * @param hasClickChange (Boolean) -> Unit
 * @param onClicked () -> Unit
 */
@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    text: String = "Sign Up with Google",
    loadingText: String = "Creating Account...",
    icon: ImageVector? = null,
    shape: Shape = ButtonDefaults.shape,
    borderColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = .3f),
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    progressIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    innerPadding: Dp = MaterialTheme.spacing.default,
    hasClick: Boolean = false,
    hasClickChange: (Boolean) -> Unit = { _ -> },
    onClicked: () -> Unit
) {
//    var clicked by remember { mutableStateOf(hasClick) }
    Surface(
        modifier = modifier.clickable {
            hasClickChange(!hasClick)
            onClicked()
        },
        shape = shape,
        border = BorderStroke(width = 1.dp, color = borderColor),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(
                    ButtonDefaults.ContentPadding
                )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ButtonDefaults.textButtonColors().contentColor
                )
            } else {
                Icon(
                    painter = painterResource(Res.drawable.ic_google_logo),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier.padding(innerPadding),
                text = if (hasClick) loadingText else text
            )
            if (hasClick) {
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = progressIndicatorColor
                )
            }
        }
    }
}

@Composable
@Preview
fun AppButtonPreview() {
    AppButton(
        text = "Button",
        onClick = {}
    )
}