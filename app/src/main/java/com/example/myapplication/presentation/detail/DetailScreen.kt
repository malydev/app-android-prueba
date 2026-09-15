package com.example.myapplication.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
internal fun DetailScreen(state: UiState<BankMovement>, back: () -> Unit, retry: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = back) { Text("‹ Volver") }
            Text("Detalle del movimiento", fontWeight = FontWeight.SemiBold)
        }
        when (state) {
            is UiState.Success -> {
                val movement = state.data
                Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Spacer(Modifier.height(16.dp)); MovementIcon(movement)
                    Text(
                        movement.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        (if (movement.amount.signum() > 0) "+ " else "− ") + money(movement.amount),
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ) {
                        Text(
                            "✓  Completado",
                            Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(22.dp),
                            verticalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            DetailField("Fecha y hora", movement.date)
                            HorizontalDivider()
                            DetailField(
                                if (movement.amount.signum() > 0) "Origen" else "Destino",
                                movement.recipient
                            )
                            DetailField("Categoría", movement.category)
                            DetailField(
                                "Tipo",
                                if (movement.amount.signum() > 0) "Abono" else "Débito"
                            )
                            DetailField("Moneda", "Bolivianos (BOB)")
                            HorizontalDivider()
                            DetailField("Referencia", movement.reference)
                        }
                    }
                    Text(
                        "Este comprobante contiene datos simulados.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            is UiState.Error -> ErrorPanel(state.message, retry)
            else -> LoadingPanel("Cargando movimiento…")
        }
    }
}

@Composable
internal fun DetailField(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(value, fontWeight = FontWeight.Medium)
    }
}
