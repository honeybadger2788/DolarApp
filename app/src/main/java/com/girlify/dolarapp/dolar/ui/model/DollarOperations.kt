package com.girlify.dolarapp.dolar.ui.model

import com.girlify.dolarapp.dolar.ui.model.DollarOperations.*

sealed class DollarOperations {
    object Compra: DollarOperations()
    object Venta: DollarOperations()
}

fun getOperations(): List<DollarOperations> = listOf(Compra, Venta)

fun operationToString(operation: DollarOperations): String {
    return when(operation) {
        Compra -> "Compra"
        Venta -> "Venta"
    }
}
