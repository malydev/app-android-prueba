package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.FakeBankRepository
import com.example.myapplication.di.BankViewModelFactory

class BankApplication : Application() {
    val viewModelFactory by lazy { BankViewModelFactory(FakeBankRepository()) }
}
