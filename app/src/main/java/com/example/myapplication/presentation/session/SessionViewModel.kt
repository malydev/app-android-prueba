package com.example.myapplication.presentation.session

import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.BankRepository

class SessionViewModel(private val repository: BankRepository) : ViewModel() {
    val authenticated = repository.authenticated

    fun logout() = repository.logout()
}
