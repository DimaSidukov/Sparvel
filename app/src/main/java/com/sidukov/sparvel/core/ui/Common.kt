package com.sidukov.sparvel.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.theme.SparvelTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onTextUpdated: (String) -> Unit
) {
    val text = remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = text.value,
        onValueChange = {
            text.value = it
            onTextUpdated(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
            .background(Color.Transparent),
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
                        text = "Search for music",
                        color = SparvelTheme.colors.textPlaceholder,
                        style = SparvelTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 17.dp)
                            .size(18.dp),
                        tint = SparvelTheme.colors.textPlaceholder,
                    )
                },
                border = {
                    TextFieldDefaults.BorderBox(
                        enabled = true,
                        isError = false,
                        interactionSource = interactionSource,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = SparvelTheme.colors.searchBorder,
                            unfocusedBorderColor = SparvelTheme.colors.searchBorder
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

            )
        },
        textStyle = SparvelTheme.typography.bodyMedium.copy(color = SparvelTheme.colors.text)
    )
}