package com.girlify.dolarapp.dolar.data.network

import com.girlify.dolarapp.dolar.data.model.DollarResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DollarService @Inject constructor(
    private val apiClient: DollarApiClient
) {

    suspend fun getDollarOficial(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response =
                apiClient.getDollarOficial()
            response.body()
        }
    }

    suspend fun getDollarBcoNacion(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response =
                apiClient.getDollarBcoNacion()
            response.body()
        }
    }

    suspend fun getDollarQatar(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response =
                apiClient.getDollarQatar()
            response.body()
        }
    }

    suspend fun getDollarTurista(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response =
                apiClient.getDollarTurista()
            response.body()
        }
    }

    suspend fun getDollarAhorro(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response =
                apiClient.getDollarAhorro()
            response.body()
        }
    }

    suspend fun getDollarBlue(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response =
                apiClient.getDollarBlue()
            response.body()
        }
    }

    suspend fun getDollarMEP(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response = apiClient.getDollarMEP()
            response.body()
        }
    }

    suspend fun getDollarCCL(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response = apiClient.getDollarCCL()
            response.body()
        }
    }
}