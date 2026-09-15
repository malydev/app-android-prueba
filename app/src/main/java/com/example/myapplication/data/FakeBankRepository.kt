package com.example.myapplication.data

import com.example.myapplication.domain.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeBankRepository : BankRepository {
    private val session = MutableStateFlow(false)
    override val authenticated = session.asStateFlow()
    private val data = DemoBankData.account
    override suspend fun login(email: String, password: String) {
        delay(900)
        if (!email.trim().equals("demo@nova.com", ignoreCase = true) || password != "Nova123") {
            throw IllegalArgumentException("Correo o contraseña incorrectos. Inténtalo de nuevo.")
        }
        session.value = true
    }
    override suspend fun account(simulateFailure: Boolean): Account {
        delay(1000)
        check(session.value) { "Tu sesión ha terminado. Inicia sesión nuevamente." }
        check(!simulateFailure) { "No pudimos cargar tu cuenta. Revisa tu conexión e inténtalo de nuevo." }
        return data
    }
    override suspend fun movement(id: String): BankMovement {
        delay(500)
        check(session.value) { "Tu sesión ha terminado." }
        return data.movements.firstOrNull { it.id == id } ?: error("No encontramos este movimiento.")
    }
    override fun logout() { session.value = false }
}
