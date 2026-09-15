package com.example.myapplication.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.myapplication.domain.*
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

private val Green = Color(0xFF175D46)
private fun money(value: BigDecimal): String = "Bs ${NumberFormat.getNumberInstance(Locale("es", "BO")).apply { minimumFractionDigits = 2; maximumFractionDigits = 2 }.format(value.abs())}"

@Composable
fun BankApp(model: BankViewModel) {
    val nav = rememberNavController()
    val login by model.login.collectAsStateWithLifecycle()
    // A restored back stack must never expose account screens after process death.
    LaunchedEffect(login) {
        if (login !is UiState.Success && nav.currentDestination?.route != "login") {
            nav.navigate("login") { popUpTo(nav.graph.id) { inclusive = true }; launchSingleTop = true }
        }
    }
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        NavHost(navController = nav, startDestination = "login", modifier = Modifier.safeDrawingPadding()) {
            composable("login") {
                LaunchedEffect(login) {
                    if (login is UiState.Success) nav.navigate("home") { popUpTo("login") { inclusive = true }; launchSingleTop = true }
                }
                LoginScreen(model)
            }
            composable("home") {
                val state by model.account.collectAsStateWithLifecycle()
                LaunchedEffect(Unit) { if (state is UiState.Idle && login is UiState.Success) model.loadAccount() }
                HomeScreen(state, { model.loadAccount() }, { model.loadAccount(true) }, { model.logout() }, { nav.navigate("movement/$it") })
            }
            composable("movement/{id}", arguments = listOf(navArgument("id") { type = NavType.StringType })) { entry ->
                val id = entry.arguments?.getString("id").orEmpty()
                val state by model.detail.collectAsStateWithLifecycle()
                LaunchedEffect(id) { if (login is UiState.Success) model.loadDetail(id) }
                DetailScreen(state, { nav.popBackStack() }, { model.loadDetail(id) })
            }
        }
    }
}

@Composable
private fun LoginScreen(model: BankViewModel) {
    val form by model.form.collectAsStateWithLifecycle()
    val state by model.login.collectAsStateWithLifecycle()
    var visible by rememberSaveable { mutableStateOf(false) }
    val loading = state is UiState.Loading
    Column(Modifier.fillMaxSize().imePadding().verticalScroll(rememberScrollState()).padding(28.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Spacer(Modifier.height(24.dp))
        Brand()
        Spacer(Modifier.height(20.dp))
        Text("Tu dinero,\nen buenas manos.", fontSize = 38.sp, lineHeight = 43.sp, fontWeight = FontWeight.Bold)
        Text("Todo lo que necesitas, en un solo lugar.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(24.dp)) {
            Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Bienvenido de nuevo", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                Text("Ingresa a tu cuenta NOVA", color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(value = form.email, onValueChange = model::emailChanged, label = { Text("Correo electrónico") }, singleLine = true,
                    enabled = !loading, isError = form.emailError != null, supportingText = form.emailError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next), modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = form.password, onValueChange = model::passwordChanged, label = { Text("Contraseña") }, singleLine = true,
                    enabled = !loading, isError = form.passwordError != null, supportingText = form.passwordError?.let { { Text(it) } },
                    visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = { TextButton(onClick = { visible = !visible }) { Text(if (visible) "Ocultar" else "Ver") } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { model.signIn() }), modifier = Modifier.fillMaxWidth())
                if (state is UiState.Error) Text((state as UiState.Error).message, color = MaterialTheme.colorScheme.error)
                Button(onClick = model::signIn, enabled = !loading, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(14.dp)) {
                    if (loading) { CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary); Spacer(Modifier.width(12.dp)) }
                    Text(if (loading) "Ingresando…" else if (state is UiState.Error) "Reintentar" else "Iniciar sesión  →")
                }
            }
        }
        Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(16.dp)) {
            Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("PRUEBA LA EXPERIENCIA", fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text("Correo: demo@nova.com\nContraseña: Nova123", lineHeight = 24.sp)
            }
        }
        Text("Entorno de demostración · Datos simulados", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun Brand() {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(Modifier.size(38.dp).background(Green, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) { Text("N", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp) }
        Text("NOVA", fontSize = 22.sp, fontWeight = FontWeight.Bold, letterSpacing = 3.sp)
    }
}

@Composable
private fun HomeScreen(state: UiState<Account>, retry: () -> Unit, simulate: () -> Unit, logout: () -> Unit, open: (String) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Brand(); TextButton(onClick = logout) { Text("Cerrar sesión") }
        }
        when (state) {
            is UiState.Success -> {
                val account = state.data
                LazyColumn(contentPadding = PaddingValues(24.dp, 8.dp, 24.dp, 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item {
                        Text("Hola, ${account.owner.substringBefore(' ')}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text("Así se mueve tu dinero hoy.", modifier = Modifier.padding(top = 6.dp, bottom = 16.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    item {
                        Card(colors = CardDefaults.cardColors(containerColor = Green), shape = RoundedCornerShape(24.dp)) {
                            Column(Modifier.fillMaxWidth().padding(26.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("SALDO DISPONIBLE", color = Color(0xFFC8DECE), fontSize = 11.sp, letterSpacing = 2.sp)
                                Text(money(account.balance), color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                                HorizontalDivider(color = Color.White.copy(alpha = .2f), modifier = Modifier.padding(vertical = 8.dp))
                                Text("Cuenta de ahorros · BOB", color = Color.White)
                                Text(account.number, color = Color(0xFFC8DECE), letterSpacing = 2.sp)
                            }
                        }
                    }
                    item {
                        Row(Modifier.fillMaxWidth().padding(top = 14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Últimos movimientos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            TextButton(onClick = retry) { Text("Actualizar") }
                        }
                    }
                    if (account.movements.isEmpty()) item { Text("Aún no tienes movimientos.") }
                    items(account.movements, key = { it.id }) { MovementRow(it) { open(it.id) } }
                    item {
                        Text("DEMOSTRACIÓN", modifier = Modifier.padding(top = 16.dp), fontSize = 11.sp, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        TextButton(onClick = simulate) { Text("Simular un error de carga") }
                    }
                }
            }
            is UiState.Error -> ErrorPanel(state.message, retry)
            else -> LoadingPanel("Cargando tu cuenta…")
        }
    }
}

@Composable
private fun MovementRow(movement: BankMovement, onClick: () -> Unit) {
    Surface(shape = RoundedCornerShape(18.dp), color = Color.White, modifier = Modifier.fillMaxWidth().clickable(onClickLabel = "Ver detalle de ${movement.title}", onClick = onClick)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MovementIcon(movement)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(movement.title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                Text(movement.date.substringBefore(" ·"), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text((if (movement.amount.signum() > 0) "+ " else "− ") + money(movement.amount), fontWeight = FontWeight.SemiBold, color = if (movement.amount.signum() > 0) Green else MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
                Text("Ver detalle ›", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun MovementIcon(movement: BankMovement) {
    Box(Modifier.size(44.dp).background(if (movement.amount.signum() > 0) Color(0xFFE0F1E5) else Color(0xFFF0F1EC), CircleShape), contentAlignment = Alignment.Center) {
        Text(if (movement.amount.signum() > 0) "↙" else "↗", fontSize = 24.sp, color = Green)
    }
}

@Composable
private fun DetailScreen(state: UiState<BankMovement>, back: () -> Unit, retry: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = back) { Text("‹ Volver") }
            Text("Detalle del movimiento", fontWeight = FontWeight.SemiBold)
        }
        when (state) {
            is UiState.Success -> {
                val movement = state.data
                Column(Modifier.verticalScroll(rememberScrollState()).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Spacer(Modifier.height(16.dp)); MovementIcon(movement)
                    Text(movement.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text((if (movement.amount.signum() > 0) "+ " else "− ") + money(movement.amount), fontSize = 34.sp, fontWeight = FontWeight.Bold)
                    Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape) { Text("✓  Completado", Modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = Green) }
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(22.dp)) {
                        Column(Modifier.fillMaxWidth().padding(22.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                            DetailField("Fecha y hora", movement.date)
                            HorizontalDivider()
                            DetailField(if (movement.amount.signum() > 0) "Origen" else "Destino", movement.recipient)
                            DetailField("Categoría", movement.category)
                            DetailField("Tipo", if (movement.amount.signum() > 0) "Abono" else "Débito")
                            DetailField("Moneda", "Bolivianos (BOB)")
                            HorizontalDivider()
                            DetailField("Referencia", movement.reference)
                        }
                    }
                    Text("Este comprobante contiene datos simulados.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            is UiState.Error -> ErrorPanel(state.message, retry)
            else -> LoadingPanel("Cargando movimiento…")
        }
    }
}

@Composable
private fun DetailField(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun LoadingPanel(message: String) {
    Column(Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        CircularProgressIndicator(); Spacer(Modifier.height(20.dp)); Text(message)
    }
}

@Composable
private fun ErrorPanel(message: String, retry: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Algo salió mal", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp)); Text(message)
        Spacer(Modifier.height(24.dp)); Button(onClick = retry) { Text("Reintentar") }
    }
}
