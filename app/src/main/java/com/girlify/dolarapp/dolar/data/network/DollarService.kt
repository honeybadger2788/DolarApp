package com.girlify.dolarapp.dolar.data.network

import com.girlify.dolarapp.core.RetrofitHelper
import com.girlify.dolarapp.dolar.data.model.DollarResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DollarService {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getDollarOficial(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(DollarApiClient::class.java).getDollarOficial()
            response.body()
        }
    }

    suspend fun getDollarBcoNacion(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(DollarApiClient::class.java).getDollarBcoNacion()
            response.body()
        }
    }

    suspend fun getDollarQatar(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(DollarApiClient::class.java).getDollarQatar()
            response.body()
        }
    }

    suspend fun getDollarTurista(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(DollarApiClient::class.java).getDollarTurista()
            response.body()
        }
    }

    suspend fun getDollarAhorro(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(DollarApiClient::class.java).getDollarAhorro()
            response.body()
        }
    }

    suspend fun getDollarBlue(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(DollarApiClient::class.java).getDollarBlue()
            response.body()
        }
    }

    suspend fun getDollarMEP(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(DollarApiClient::class.java).getDollarMEP()
            response.body()
        }
    }

    suspend fun getDollarCCL(): DollarResponse? {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(DollarApiClient::class.java).getDollarCCL()
            response.body()
        }
    }
}