package com.girlify.dolarapp.core

import com.girlify.dolarapp.dolar.data.network.DollarApiClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://mercados.ambito.com//")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideDollar(retrofit: Retrofit): DollarApiClient {
        return retrofit.create(DollarApiClient::class.java)
    }
}