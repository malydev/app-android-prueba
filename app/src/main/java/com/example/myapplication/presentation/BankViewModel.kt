package com.example.myapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
data class LoginForm(val email: String = "", val password: String = "", val emailError: String? = null, val passwordError: String? = null)

class BankViewModel(private val repository: BankRepository) : ViewModel() {
    private val _form = MutableStateFlow(LoginForm())
    val form = _form.asStateFlow()
    private val _login = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val login = _login.asStateFlow()
    private val _account = MutableStateFlow<UiState<Account>>(UiState.Idle)
    val account = _account.asStateFlow()
    private val _detail = MutableStateFlow<UiState<BankMovement>>(UiState.Idle)
    val detail = _detail.asStateFlow()
    private var accountJob: Job? = null
    private var detailJob: Job? = null
    fun emailChanged(value: String) { _form.value = _form.value.copy(email = value, emailError = null); _login.value = UiState.Idle }
    fun passwordChanged(value: String) { _form.value = _form.value.copy(password = value, passwordError = null); _login.value = UiState.Idle }
    fun signIn() {
        if (_login.value == UiState.Loading) return
        val input = _form.value
        val emailError = LoginValidator.emailError(input.email)
        val passwordError = LoginValidator.passwordError(input.password)
        _form.value = input.copy(emailError = emailError, passwordError = passwordError)
        if (emailError != null || passwordError != null) return
        _login.value = UiState.Loading
        viewModelScope.launch {
            _login.value = result { repository.login(input.email, input.password) }
            if (_login.value is UiState.Success) _form.value = LoginForm()
        }
    }
    fun loadAccount(simulateFailure: Boolean = false) {
        if (accountJob?.isActive == true) return
        _account.value = UiState.Loading
        accountJob = viewModelScope.launch { _account.value = result { repository.account(simulateFailure) } }
    }
    fun loadDetail(id: String) {
        detailJob?.cancel()
        _detail.value = UiState.Loading
        detailJob = viewModelScope.launch { _detail.value = result { repository.movement(id) } }
    }
    fun logout() {
        accountJob?.cancel(); detailJob?.cancel(); repository.logout()
        _login.value = UiState.Idle; _account.value = UiState.Idle; _detail.value = UiState.Idle; _form.value = LoginForm()
    }
    private suspend fun <T> result(block: suspend () -> T): UiState<T> = try {
        UiState.Success(block())
    } catch (error: CancellationException) { throw error
    } catch (error: Exception) { UiState.Error(error.message ?: "Ocurrió un error. Inténtalo de nuevo.") }

    class Factory(private val repository: BankRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(BankViewModel::class.java))
            return BankViewModel(repository) as T
        }
    }
}
