package com.example.myapplication.presentation.common

import kotlinx.coroutines.CancellationException

internal suspend fun <T> loadResult(block: suspend () -> T): UiState<T> = try {
    UiState.Success(block())
} catch (error: CancellationException) {
    throw error
} catch (error: Exception) {
    UiState.Error(error.message ?: "Ocurrió un error. Inténtalo de nuevo.")
}
