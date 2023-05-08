package com.girlify.dolarapp.dolar.data.network

import com.girlify.dolarapp.dolar.data.model.DollarResponse
import javax.inject.Inject

class DollarService @Inject constructor(
    private val apiClient: DollarApiClient
) {

    suspend fun getDollarOficial(): DollarResponse? {
            val response =
                apiClient.getDollarOficial()
        return response.body()
    }


    suspend fun getDollarBcoNacion(): DollarResponse? {
        val response =
            apiClient.getDollarBcoNacion()
        return response.body()
    }

    suspend fun getDollarQatar(): DollarResponse? {
        val response =
            apiClient.getDollarQatar()
        return response.body()
    }

    suspend fun getDollarTurista(): DollarResponse? {
        val response =
            apiClient.getDollarTurista()
        return response.body()
    }

    suspend fun getDollarAhorro(): DollarResponse? {
        val response =
            apiClient.getDollarAhorro()
        return response.body()
    }

    suspend fun getDollarBlue(): DollarResponse? {
        val response =
            apiClient.getDollarBlue()
        return response.body()
    }

    suspend fun getDollarMEP(): DollarResponse? {
        val response =
            apiClient.getDollarMEP()
        return response.body()
    }

    suspend fun getDollarCCL(): DollarResponse? {
        val response =
            apiClient.getDollarCCL()
        return response.body()
    }
}