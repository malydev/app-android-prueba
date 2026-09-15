package com.example.myapplication.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.*
import com.example.myapplication.presentation.common.*
import com.example.myapplication.presentation.components.*

@Composable
internal fun LoginScreen(
    form: LoginForm,
    state: UiState<Unit>,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSignIn: () -> Unit
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    val loading = state is UiState.Loading
    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(28.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Brand()
        Spacer(Modifier.height(20.dp))
        Text(
            "Tu dinero,\nen buenas manos.",
            fontSize = 38.sp,
            lineHeight = 43.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Todo lo que necesitas, en un solo lugar.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    "Bienvenido de nuevo",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text("Ingresa a tu cuenta NOVA", color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(
                    value = form.email,
                    onValueChange = onEmailChanged,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    enabled = !loading,
                    isError = form.emailError != null,
                    supportingText = form.emailError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = form.password,
                    onValueChange = onPasswordChanged,
                    label = { Text("Contraseña") },
                    singleLine = true,
                    enabled = !loading,
                    isError = form.passwordError != null,
                    supportingText = form.passwordError?.let { { Text(it) } },
                    visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = {
                            visible = !visible
                        }) { Text(if (visible) "Ocultar" else "Ver") }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { onSignIn() }),
                    modifier = Modifier.fillMaxWidth()
                )
                if (state is UiState.Error) Text(
                    state.message,
                    color = MaterialTheme.colorScheme.error
                )
                Button(
                    onClick = onSignIn,
                    enabled = !loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        ); Spacer(Modifier.width(12.dp))
                    }
                    Text(if (loading) "Ingresando…" else if (state is UiState.Error) "Reintentar" else "Iniciar sesión  →")
                }
            }
        }
    }
}
