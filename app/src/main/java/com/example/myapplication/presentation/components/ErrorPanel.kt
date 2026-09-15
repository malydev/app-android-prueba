package com.example.myapplication.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.domain.*
import com.example.myapplication.presentation.common.*
import com.example.myapplication.presentation.components.*

@Composable
internal fun ErrorPanel(message: String, retry: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Algo salió mal",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp)); Text(message)
        Spacer(Modifier.height(24.dp)); Button(onClick = retry) { Text("Reintentar") }
    }
}
