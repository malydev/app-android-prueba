package com.example.myapplication.domain

import java.math.BigDecimal

data class BankMovement(
    val id: String,
    val title: String,
    val category: String,
    val date: String,
    val amount: BigDecimal,
    val reference: String,
    val recipient: String
)
