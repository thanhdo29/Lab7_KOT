package com.example.lab7

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {
    private val _movie=MutableLiveData<List<Movie>>()
    val movies:LiveData<List<Movie>> = _movie

    init {
        _movie.value=Movie.getSampleMovies()
    }
}