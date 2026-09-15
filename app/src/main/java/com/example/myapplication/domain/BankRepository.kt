package com.example.myapplication.domain

import java.math.BigDecimal

data class BankMovement(
    val id: String,
    val title: String,
    val category: String,
    val date: String,
    val amount: BigDecimal,
    val reference: String,
    val recipient: String
)
data class Account(val owner: String, val number: String, val balance: BigDecimal, val movements: List<BankMovement>)
interface BankRepository {
    suspend fun login(email: String, password: String)
    suspend fun account(simulateFailure: Boolean = false): Account
    suspend fun movement(id: String): BankMovement
    fun logout()
}

object LoginValidator {
    fun emailError(email: String): String? = when {
        email.isBlank() -> "Ingresa tu correo electrónico"
        !Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(email.trim()) -> "Ingresa un correo válido"
        else -> null
    }
    fun passwordError(password: String): String? = when {
        password.isBlank() -> "Ingresa tu contraseña"
        password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
        else -> null
    }
}
