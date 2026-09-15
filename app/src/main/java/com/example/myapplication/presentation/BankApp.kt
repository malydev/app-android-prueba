package com.example.myapplication.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myapplication.domain.BankRepository
import com.example.myapplication.presentation.navigation.BankNavHost

@Composable
fun BankApp(repository: BankRepository) {
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        BankNavHost(repository)
    }
}
