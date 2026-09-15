package com.example.myapplication.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.*
import com.example.myapplication.presentation.common.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: BankRepository) : ViewModel() {
    private val _form = MutableStateFlow(LoginForm())
    val form = _form.asStateFlow()
    private val _login = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val login = _login.asStateFlow()

    fun emailChanged(value: String) {
        _form.value = _form.value.copy(email = value, emailError = null)
        _login.value = UiState.Idle
    }

    fun passwordChanged(value: String) {
        _form.value = _form.value.copy(password = value, passwordError = null)
        _login.value = UiState.Idle
    }

    fun signIn() {
        if (_login.value == UiState.Loading) return
        val input = _form.value
        val emailError = LoginValidator.emailError(input.email)
        val passwordError = LoginValidator.passwordError(input.password)
        _form.value = input.copy(emailError = emailError, passwordError = passwordError)
        if (emailError != null || passwordError != null) return
        _login.value = UiState.Loading
        viewModelScope.launch {
            _login.value = loadResult { repository.login(input.email, input.password) }
            if (_login.value is UiState.Success) _form.value = LoginForm()
        }
    }
}
