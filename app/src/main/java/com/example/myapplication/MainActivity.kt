package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.FakeBankRepository
import com.example.myapplication.presentation.BankApp
import com.example.myapplication.presentation.BankViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val model: BankViewModel = viewModel(factory = BankViewModel.Factory(FakeBankRepository()))
                BankApp(model)
            }
        }
    }
}
