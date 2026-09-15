package com.example.myapplication.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.*
import com.example.myapplication.presentation.common.*
import com.example.myapplication.presentation.components.*

@Composable
internal fun HomeScreen(
    state: UiState<Account>,
    retry: () -> Unit,
    simulate: () -> Unit,
    logout: () -> Unit,
    open: (String) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Brand(); TextButton(onClick = logout) { Text("Cerrar sesión") }
        }
        when (state) {
            is UiState.Success -> {
                val account = state.data
                LazyColumn(
                    contentPadding = PaddingValues(24.dp, 8.dp, 24.dp, 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            "Hola, ${account.owner.substringBefore(' ')}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Así se mueve tu dinero hoy.",
                            modifier = Modifier.padding(top = 6.dp, bottom = 16.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    item {
                        BalanceCard(account)
                    }
                    item {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Últimos movimientos",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            TextButton(onClick = retry) { Text("Actualizar") }
                        }
                    }
                    if (account.movements.isEmpty()) item { Text("Aún no tienes movimientos.") }
                    items(account.movements, key = { it.id }) { MovementRow(it) { open(it.id) } }
                    item {
                        Text(
                            "DEMOSTRACIÓN",
                            modifier = Modifier.padding(top = 16.dp),
                            fontSize = 11.sp,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(onClick = simulate) { Text("Simular un error de carga") }
                    }
                }
            }

            is UiState.Error -> ErrorPanel(state.message, retry)
            else -> LoadingPanel("Cargando tu cuenta…")
        }
    }
}
