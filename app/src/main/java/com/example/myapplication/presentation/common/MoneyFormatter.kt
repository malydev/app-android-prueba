package com.example.myapplication.presentation.common

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

internal fun money(value: BigDecimal): String = "Bs ${NumberFormat.getNumberInstance(Locale("es", "BO")).apply {
    minimumFractionDigits = 2
    maximumFractionDigits = 2
}.format(value.abs())}"
