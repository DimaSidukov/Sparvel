package com.sidukov.sparvel.features.home

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    ModalDrawerSheet(
        modifier = Modifier.fillMaxHeight().fillMaxWidth(0.7f)
    ) {
        Text(text = "test")
        Button(onClick = { /*TODO*/ }) {
            Text(text = "test2")
        }
        Text("test3")
    }


}