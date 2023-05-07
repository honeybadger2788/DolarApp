package com.girlify.dolarapp.dolar.data

import com.girlify.dolarapp.dolar.data.network.DollarService
import com.girlify.dolarapp.dolar.ui.model.DollarModel
import javax.inject.Inject

class DollarRepository @Inject constructor(
    private val api: DollarService
) {
    
    suspend fun getDollarBlue(): DollarModel? {
        val response = api.getDollarBlue()
        return response?.toDomain("Blue")
    }
    suspend fun getDollarOficial(): DollarModel? {
        val response = api.getDollarOficial()
        return response?.toDomain("Oficial")
    }
    suspend fun getDollarBcoNacion(): DollarModel? {
        val response = api.getDollarBcoNacion()
        return response?.toDomain("Banco Nacion")
    }
    suspend fun getDollarMEP(): DollarModel? {
        val response = api.getDollarMEP()
        return response?.toDomain("MEP")
    }
    suspend fun getDollarCCL(): DollarModel? {
        val response = api.getDollarCCL()
        return response?.toDomain("CCL")
    }
    suspend fun getDollarAhorro(): DollarModel? {
        val response = api.getDollarAhorro()
        return response?.toDomain("Ahorro")
    }
    suspend fun getDollarTurista(): DollarModel? {
        val response = api.getDollarTurista()
        return response?.toDomain("Turista")
    }
    suspend fun getDollarQatar(): DollarModel? {
        val response = api.getDollarQatar()
        return response?.toDomain("Qatar")
    }
}