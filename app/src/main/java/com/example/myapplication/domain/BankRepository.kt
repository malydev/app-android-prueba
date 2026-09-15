package com.example.myapplication.domain

import kotlinx.coroutines.flow.StateFlow

interface BankRepository {
    val authenticated: StateFlow<Boolean>
    suspend fun login(email: String, password: String)
    suspend fun account(simulateFailure: Boolean = false): Account
    suspend fun movement(id: String): BankMovement
    fun logout()
}

