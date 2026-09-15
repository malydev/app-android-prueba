package com.example.myapplication.domain

import java.math.BigDecimal

data class Account(val owner: String, val number: String, val balance: BigDecimal, val movements: List<BankMovement>)
