package com.example.lab7

import retrofit2.Response
import retrofit2.http.GET

interface MovieService {
    @GET("list-film.php")
    suspend fun getListFilm():Response<List<MovieResponse>>
}