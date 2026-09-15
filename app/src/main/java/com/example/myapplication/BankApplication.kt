package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.FakeBankRepository
import com.example.myapplication.domain.BankRepository

class BankApplication : Application() {
    val repository: BankRepository by lazy { FakeBankRepository() }
}
