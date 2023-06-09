package com.girlify.dolarapp.ui.composables;

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ErrorComponent(msg: String) {
        Box(Modifier.fillMaxSize()) {
                Text(text = msg, Modifier.align(Alignment.Center))
        }
}
