package com.example.myapplication.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.*
import com.example.myapplication.presentation.common.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: BankRepository) : ViewModel() {
    private val _account = MutableStateFlow<UiState<Account>>(UiState.Idle)
    val account = _account.asStateFlow()
    private var accountJob: Job? = null
    fun loadAccount(simulateFailure: Boolean = false) {
        if (accountJob?.isActive == true) return
        _account.value = UiState.Loading
        accountJob = viewModelScope.launch { _account.value = loadResult { repository.account(simulateFailure) } }
    }
}
