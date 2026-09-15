package com.example.myapplication.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.*
import com.example.myapplication.presentation.common.*
import com.example.myapplication.presentation.components.*

@Composable
internal fun MovementIcon(movement: BankMovement) {
    Box(Modifier.size(44.dp).background(if (movement.amount.signum() > 0) Color(0xFFE0F1E5) else Color(0xFFF0F1EC), CircleShape), contentAlignment = Alignment.Center) {
        Text(if (movement.amount.signum() > 0) "↙" else "↗", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
    }
}
