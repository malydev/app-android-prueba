package com.example.myapplication.presentation.login

data class LoginForm(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null
)

