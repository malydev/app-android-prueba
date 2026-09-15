package com.example.myapplication.data

import com.example.myapplication.domain.*
import java.math.BigDecimal

internal object DemoBankData {
    val account = Account("Alex Morgan", "••••  ••••  ••••  4829", BigDecimal("24850.75"), listOf(
        BankMovement("1", "Supermercado", "Compras", "15 sep 2026 · 10:42", BigDecimal("-285.50"), "NOVA-260915-001", "Supermercado Hipermaxi"),
        BankMovement("2", "Pago de salario", "Ingreso", "14 sep 2026 · 09:00", BigDecimal("8500.00"), "NOVA-260914-002", "Empresa Digital S.R.L."),
        BankMovement("3", "Café de la mañana", "Restaurantes", "14 sep 2026 · 08:15", BigDecimal("-32.00"), "NOVA-260914-003", "Café de especialidad"),
        BankMovement("4", "Transferencia recibida", "Transferencias", "13 sep 2026 · 16:30", BigDecimal("450.00"), "NOVA-260913-004", "María López"),
        BankMovement("5", "Internet del hogar", "Servicios", "12 sep 2026 · 12:05", BigDecimal("-249.00"), "NOVA-260912-005", "Proveedor de internet"),
        BankMovement("6", "Transporte", "Transporte", "11 sep 2026 · 18:20", BigDecimal("-24.50"), "NOVA-260911-006", "Servicio de transporte")
    ))
}
