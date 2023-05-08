package com.girlify.dolarapp.dolar.data

import com.girlify.dolarapp.dolar.data.network.DollarService
import com.girlify.dolarapp.dolar.ui.model.DollarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DollarRepository @Inject constructor(
    private val api: DollarService
) {
    fun getDollars(): Flow<List<DollarModel>> = flow {
        emit(getDollarList())
        while (true) {
            delay(5000) // Actualiza cada 5 segundos
            emit(getDollarList())
        }
    }

    private suspend fun getDollarList(): List<DollarModel> = withContext(Dispatchers.IO) {
        val deferredDollars = listOf(
            async { api.getDollarBcoNacion()?.toDomain("Bco Nacion") },
            async { api.getDollarAhorro()?.toDomain("Ahorro") },
            async { api.getDollarBlue()?.toDomain("Blue") },
            async { api.getDollarMEP()?.toDomain("MEP") },
            async { api.getDollarCCL()?.toDomain("CCL") },
            async { api.getDollarQatar()?.toDomain("Qatar") },
            async { api.getDollarOficial()?.toDomain("Oficial") },
            async { api.getDollarTurista()?.toDomain("Turista") }
        )
        deferredDollars.awaitAll().filterNotNull()
    }
}