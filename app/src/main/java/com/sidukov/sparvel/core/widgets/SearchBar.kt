package com.sidukov.sparvel.core.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.checkKeyboardState
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.core.widgets.CloseButtonState.HIDDEN
import com.sidukov.sparvel.core.widgets.CloseButtonState.SHOWN

internal enum class CloseButtonState {
    SHOWN, HIDDEN
}

internal val animationSpec = tween<Float>(500)

@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = stringResource(R.string.search_bar_hint),
    onTextUpdated: (String) -> Unit = { }
) {
    val text = remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var closeButtonState by rememberSaveable {
        mutableStateOf(HIDDEN)
    }

    val closeButtonOffset by animateFloatAsState(
        targetValue = if (closeButtonState == HIDDEN) -30f else 0f,
        animationSpec = animationSpec
    )

    val closeButtonRotation by animateFloatAsState(
        targetValue = if (closeButtonState == HIDDEN) 0f else 45f,
        animationSpec = animationSpec
    )

    val closeButtonAlpha by animateFloatAsState(
        targetValue = if (closeButtonState == HIDDEN) 0f else 1f,
        animationSpec = animationSpec
    )

    var isSearchBoxFocused by rememberSaveable { mutableStateOf(false) }

    checkKeyboardState { isKeyboardOpen ->
        closeButtonState = if (isKeyboardOpen && isSearchBoxFocused) SHOWN else HIDDEN
    }

    CompositionLocalProvider(
        LocalTextSelectionColors provides TextSelectionColors(
            handleColor = SparvelTheme.colors.cursor,
            backgroundColor = SparvelTheme.colors.cursor.copy(alpha = 0.4f)
        )
    ) {
        BasicTextField(
            value = text.value,
            onValueChange = {
                if (closeButtonState == HIDDEN || it.isEmpty()) closeButtonState = SHOWN
                text.value = it
                onTextUpdated(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .then(modifier)
                .background(Color.Transparent)
                .onFocusChanged {
                    isSearchBoxFocused = it.isFocused
                },
            cursorBrush = SolidColor(SparvelTheme.colors.cursor),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }),
            singleLine = true,
            decorationBox = @Composable { textField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = text.value,
                    innerTextField = textField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    placeholder = {
                        Text(
                            text = hint,
                            color = SparvelTheme.colors.textPlaceholder,
                            style = SparvelTheme.typography.searchBar
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 20.dp, end = 17.dp)
                                .size(18.dp),
                            tint = SparvelTheme.colors.searchText,
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_plus),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .graphicsLayer {
                                    alpha = closeButtonAlpha
                                    translationX = closeButtonOffset
                                    rotationZ = closeButtonRotation
                                }
                                .clickable(
                                    enabled = closeButtonState == SHOWN,
                                    interactionSource = remember {
                                        MutableInteractionSource()
                                    },
                                    indication = null
                                ) {
                                    closeButtonState = HIDDEN
                                    text.value = ""
                                    onTextUpdated(text.value)
                                }
                                .fillMaxHeight()
                                .padding(12.dp),
                            tint = SparvelTheme.colors.searchText
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(),
                    contentPadding = PaddingValues(0.dp),
                    container = {
                        OutlinedTextFieldDefaults.ContainerBox(
                            enabled = true,
                            isError = false,
                            interactionSource = interactionSource,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SparvelTheme.colors.searchBorder,
                                unfocusedBorderColor = SparvelTheme.colors.searchBorder,
                            ),
                            shape = RoundedCornerShape(10.dp),
                            focusedBorderThickness = OutlinedTextFieldDefaults.FocusedBorderThickness,
                            unfocusedBorderThickness = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
                        )
                    },
                )
            },
            textStyle = SparvelTheme.typography.searchBar.copy(color = SparvelTheme.colors.text)
        )
    }

}