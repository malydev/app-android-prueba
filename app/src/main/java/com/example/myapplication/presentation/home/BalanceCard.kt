package com.example.myapplication.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.*
import com.example.myapplication.presentation.common.*
import com.example.myapplication.presentation.components.*

@Composable
internal fun BalanceCard(account: Account) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary), shape = RoundedCornerShape(24.dp)) {
        Column(Modifier.fillMaxWidth().padding(26.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("SALDO DISPONIBLE", color = Color(0xFFC8DECE), fontSize = 11.sp, letterSpacing = 2.sp)
            Text(money(account.balance), color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
            HorizontalDivider(color = Color.White.copy(alpha = .2f), modifier = Modifier.padding(vertical = 8.dp))
            Text("Cuenta de ahorros · BOB", color = Color.White)
            Text(account.number, color = Color(0xFFC8DECE), letterSpacing = 2.sp)
        }
    }
}
