package com.sidukov.sparvel.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.theme.SparvelTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = stringResource(R.string.search_bar_hint),
    onTextUpdated: (String) -> Unit
) {
    val text = remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = text.value,
        onValueChange = {
            text.value = it
            onTextUpdated(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .then(modifier)
            .background(Color.Transparent),
        cursorBrush = SolidColor(SparvelTheme.colors.cursor),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }),
        singleLine = true,
        decorationBox = @Composable { textField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
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
                border = {
                    TextFieldDefaults.BorderBox(
                        enabled = true,
                        isError = false,
                        interactionSource = interactionSource,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = SparvelTheme.colors.searchBorder,
                            unfocusedBorderColor = SparvelTheme.colors.searchBorder,
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                },
                contentPadding = PaddingValues(0.dp)
            )
        },
        textStyle = SparvelTheme.typography.searchBar.copy(color = SparvelTheme.colors.text)
    )
}