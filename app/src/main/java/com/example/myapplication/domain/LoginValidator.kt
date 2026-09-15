package com.example.myapplication.domain

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
