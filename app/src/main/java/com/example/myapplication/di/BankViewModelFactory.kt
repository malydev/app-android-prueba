package com.example.myapplication.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.domain.BankRepository
import com.example.myapplication.presentation.login.LoginViewModel
import com.example.myapplication.presentation.home.HomeViewModel
import com.example.myapplication.presentation.detail.DetailViewModel
import com.example.myapplication.presentation.session.SessionViewModel

class BankViewModelFactory(private val repository: BankRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        LoginViewModel::class.java -> LoginViewModel(repository)
        HomeViewModel::class.java -> HomeViewModel(repository)
        DetailViewModel::class.java -> DetailViewModel(repository)
        SessionViewModel::class.java -> SessionViewModel(repository)
        else -> error("Unknown ViewModel: ${modelClass.name}")
    } as T
}
