/*
 *  Created by aiyu
 *  Copyright (c) 2021 . All rights reserved.
 *  BIT App
 *
 */

package com.atech.expensesync.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntSize
import com.atech.expensesync.ui_utils.formatAmount
import com.atech.expensesync.ui_utils.isValidDecimalInput

/**
 * Edit text
 * Custom edit text
 *
 * @param modifier Modifier
 * @param value String default value
 * @param placeholder String placeholder
 * @param onValueChange (String) -> Unit action to perform when the value changes
 * @param clearIconClick () -> Unit action to perform when the clear icon is clicked
 * @param isError Boolean value to determine if the edit text has an error
 * @param errorMessage String error message
 * @param supportingMessage String supporting message
 * @param keyboardOptions KeyboardOptions
 * @param focusRequester FocusRequester
 * @param enable Boolean value to determine if the edit text is enabled
 * @param colors TextFieldColors
 * @param leadingIcon (@Composable () -> Unit)? leading icon
 * @param trailingIcon @Composable (() -> Unit)? trailing icon
 * @param maxLines Int maximum lines
 * @param readOnly Boolean value to determine if the edit text is read only
 * @param interactionSource MutableInteractionSource
 */
@Composable
fun EditText(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit = {},
    clearIconClick: () -> Unit = {},
    isError: Boolean = false,
    errorMessage: String = "",
    supportingMessage: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    focusRequester: FocusRequester? = null,
    enable: Boolean = true,
    colors: TextFieldColors = textFieldColors(),
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = {
        if (value.isNotBlank()) Icon(
            imageVector = Icons.Outlined.Clear,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                clearIconClick()
            })
    },
    maxLines: Int = Int.MAX_VALUE,
    readOnly: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {

    LaunchedEffect(focusRequester) {
//        awaitFrame()
        focusRequester?.requestFocus()
    }

    OutlinedTextField(
        modifier = modifier.let {
            if (focusRequester == null) it
            else it.focusRequester(focusRequester)
        },
        value = value,
        maxLines = maxLines,
        onValueChange = onValueChange,
        label = {
            Text(text = placeholder)
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = colors,
        isError = isError,
        supportingText = {
            Text(
                text = if (isError) errorMessage else supportingMessage,
            )
        },
        keyboardOptions = keyboardOptions,
        enabled = enable,
        readOnly = readOnly,
        interactionSource = interactionSource,
    )
}

/**
 * Edit text enhance
 *
 * @param modifier Modifier
 * @param value String default value
 * @param placeholder String placeholder
 * @param onValueChange (String) -> Unit action to perform when the value changes
 * @param clearIconClick () -> Unit action to perform when the clear icon is clicked
 * @param isError Boolean value to determine if the edit text has an error
 * @param supportingText @Composable (() -> Unit)? supporting text
 * @param keyboardOptions KeyboardOptions
 * @param focusRequester FocusRequester
 * @param enable Boolean value to determine if the edit text is enabled
 * @param colors TextFieldColors
 * @param leadingIcon (@Composable () -> Unit)? leading icon
 * @param trailingIcon @Composable (() -> Unit)? trailing icon
 * @param maxLines Int maximum lines
 * @param readOnly Boolean value to determine if the edit text is read only
 * @param keyboardActions KeyboardActions
 * @param interactionSource MutableInteractionSource
 * @param singleLine Boolean value to determine if the edit text is single line
 */
@Composable
fun EditTextEnhance(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit = {},
    clearIconClick: () -> Unit = {},
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    focusRequester: FocusRequester? = null,
    enable: Boolean = true,
    colors: TextFieldColors = textFieldColors(),
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = {
        if (value.isNotBlank()) Icon(
            imageVector = Icons.Outlined.Clear,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                clearIconClick()
            })
    },
    maxLines: Int = Int.MAX_VALUE,
    readOnly: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    singleLine: Boolean = false
) {

    LaunchedEffect(focusRequester) {
//        awaitFrame()
        focusRequester?.requestFocus()
    }

    OutlinedTextField(
        modifier = modifier.let {
        if (focusRequester == null) it
        else it.focusRequester(focusRequester)
    },
        value = value,
        maxLines = maxLines,
        onValueChange = onValueChange,
        label = {
            Text(text = placeholder)
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = colors,
        isError = isError,
        supportingText = supportingText,
        keyboardOptions = keyboardOptions,
        enabled = enable,
        readOnly = readOnly,
        interactionSource = interactionSource,
        keyboardActions = keyboardActions,
        singleLine = singleLine
    )
}

//@Composable
//fun MutableInteractionSource.clickable(
//    action: () -> Unit
//) = this.also { interactionSource ->
//    LaunchedEffect(key1 = interactionSource) {
//        interactionSource.interactions.collect {
//            if (it is PressInteraction.Release) {
//                action()
//            }
//        }
//    }
//
//}

/**
 * Password edit text compose
 * Custom password edit text
 *
 * @param modifier Modifier
 * @param value String default value
 * @param placeholder String placeholder
 * @param imeAction ImeAction
 * @param onValueChange (String) -> Unit action to perform when the value changes
 */
@Composable
fun PasswordEditTextCompose(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    imeAction: ImeAction = ImeAction.Next,
    onValueChange: (String) -> Unit = {}
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = value, onValueChange = onValueChange, modifier = modifier, placeholder = {
            Text(placeholder)
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, imeAction = imeAction
        ), trailingIcon = {
            if (value.isNotBlank()) {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Outlined.Visibility
                        else Icons.Outlined.VisibilityOff, contentDescription = null
                    )
                }
            }
        }, visualTransformation = if (isPasswordVisible) VisualTransformation.None
        else PasswordVisualTransformation()
    )
}

/**
 * Text field colors
 */
@Composable
fun textFieldColors() = TextFieldDefaults.colors(
    cursorColor = MaterialTheme.colorScheme.primary,
    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
    unfocusedContainerColor = MaterialTheme.colorScheme.surface
)

@Composable
fun EditTextPrice(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit = {},
    clearIconClick: () -> Unit = {},
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    focusRequester: FocusRequester? = null,
    enable: Boolean = true,
    colors: TextFieldColors = textFieldColors(),
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = {
        if (value.isNotBlank()) Icon(
            imageVector = Icons.Outlined.Clear,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                clearIconClick()
            })
    },
    maxLines: Int = Int.MAX_VALUE,
    readOnly: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    singleLine: Boolean = false
) {
    var editablePrice by remember {
        mutableStateOf(value.ifBlank { 0.0.formatAmount() })
    }

    // Update editablePrice when value changes from parent
    LaunchedEffect(value) {
        if (value != editablePrice && (value == "0.0" || value == "0")) {
            editablePrice = value
        }
    }

    EditTextEnhance(
        modifier = modifier,
        value = if (editablePrice == "0" || editablePrice == "0.0") "" else editablePrice,
        placeholder = placeholder,
        onValueChange = { newValue ->
            if (newValue.isValidDecimalInput()) {
                if (newValue.isBlank()) {
                    editablePrice = 0.0.formatAmount()
                    onValueChange("0.0")
                    return@EditTextEnhance
                }
                editablePrice = newValue
                onValueChange(newValue)
            }
        },
        clearIconClick = {
            editablePrice = ""  // Change this to directly set empty string
            onValueChange("0.0")  // Still notify parent with the numeric value
            clearIconClick.invoke()
        },
        isError = isError,
        supportingText = supportingText,
        keyboardOptions = keyboardOptions.copy(
            keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done
        ),
        focusRequester = focusRequester,
        enable = enable,
        colors = colors,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        maxLines = maxLines,
        readOnly = readOnly,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine
    )
}


@Composable
fun DropdownMenu(
    modifier: Modifier = Modifier,
    selectedOption: String,
    placeholder: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    enable: Boolean = true,
    colors: TextFieldColors = textFieldColors(),
    leadingIcon: (@Composable () -> Unit)? = null,
    readOnly: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f, label = "rotation"
    )

    // Use this to track the text field's size
    val textFieldSize = remember { mutableStateOf(IntSize.Zero) }

    Box(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().onGloballyPositioned { coordinates ->
                textFieldSize.value = coordinates.size
            },
            value = selectedOption,
            onValueChange = { },
            readOnly = readOnly,
            label = {
                Text(text = placeholder)
            },
            leadingIcon = leadingIcon,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown arrow",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.rotate(rotationState)
                    )
                }
            },
            colors = colors,
            isError = isError,
            supportingText = supportingText,
            enabled = enable,
            interactionSource = interactionSource,
            singleLine = true
        )

        // Transparent clickable overlay
        if (enable) {
            Box(
                modifier = Modifier.matchParentSize().clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    expanded = !expanded
                })
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) {
                    textFieldSize.value.width.toDp()
                }).background(MaterialTheme.colorScheme.surface)
        ) {
            options.forEach { option ->
                Column {
                    DropdownMenuItem(
                        text = { Text(option) }, onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }, colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurface,
                        leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        trailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.38f
                        ),
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.38f
                        )
                    )
                    )
                    if (option != options.last()) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth()
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                        )
                    }
                }
            }
        }
    }
}