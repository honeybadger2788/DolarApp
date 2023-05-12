package com.girlify.dolarapp.dolar.ui.dollar.model

import com.girlify.dolarapp.dolar.ui.dollar.model.DollarOperations.*

sealed class DollarOperations {
    object Buy: DollarOperations()
    object Sell: DollarOperations()
}

fun getOperations(): List<DollarOperations> = listOf(Buy, Sell)

fun operationToString(operation: DollarOperations): String {
    return when(operation) {
        Buy -> "Comprador"
        Sell -> "Vendedor"
    }
}
