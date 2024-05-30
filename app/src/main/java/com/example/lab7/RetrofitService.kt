package com.example.lab7

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

open class RetrofitService {
    private val retrofit:Retrofit=Retrofit.Builder()
        .baseUrl("")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val movieService:MovieService by lazy {
        retrofit.create(MovieService::class.java)
    }
}