package com.girlify.dolarapp.dolar.data.network

import com.girlify.dolarapp.dolar.data.model.DollarResponse
import retrofit2.Response
import retrofit2.http.GET

interface DollarApiClient {

    @GET("/dolarnacion/variacion")
    suspend fun getDollarBcoNacion(): Response<DollarResponse>

    @GET("/dolarqatar/variacion")
    suspend fun getDollarQatar(): Response<DollarResponse>

    @GET("/dolarturista/variacion")
    suspend fun getDollarTurista(): Response<DollarResponse>

    @GET("/dolarahorro/variacion")
    suspend fun getDollarAhorro(): Response<DollarResponse>

    @GET("/dolar/oficial/variacion")
    suspend fun getDollarOficial(): Response<DollarResponse>

    @GET("/dolar/informal/variacion")
    suspend fun getDollarBlue(): Response<DollarResponse>

    @GET("/dolarrava/mep/variacion")
    suspend fun getDollarMEP(): Response<DollarResponse>

    @GET("/dolarrava/cl/variacion")
    suspend fun getDollarCCL(): Response<DollarResponse>
}