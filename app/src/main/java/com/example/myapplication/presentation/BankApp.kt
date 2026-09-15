package com.example.myapplication.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myapplication.di.BankViewModelFactory
import com.example.myapplication.presentation.navigation.BankNavHost

@Composable
fun BankApp(factory: BankViewModelFactory) {
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        BankNavHost(factory)
    }
}
