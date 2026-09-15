package com.example.myapplication.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.*
import com.example.myapplication.presentation.common.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: BankRepository) : ViewModel() {
    private val _detail = MutableStateFlow<UiState<BankMovement>>(UiState.Idle)
    val detail = _detail.asStateFlow()
    private var detailJob: Job? = null
    fun loadDetail(id: String) {
        detailJob?.cancel()
        _detail.value = UiState.Loading
        detailJob = viewModelScope.launch { _detail.value = loadResult { repository.movement(id) } }
    }
}
