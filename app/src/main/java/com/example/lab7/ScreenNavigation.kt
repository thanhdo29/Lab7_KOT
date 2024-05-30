package com.example.lab7

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ScreenNavigation(){
    val navController= rememberNavController()

    val mainViewModel:MainViewModel= viewModel()
    val movieState=mainViewModel.movies.observeAsState(initial = emptyList())
    NavHost(navController = navController, startDestination = Screen.LOGIN.router){
        composable(Screen.SCREEN1.router){
            Screen1(navController = navController)
        }
        composable(Screen.SCREEN2.router){
            Screen2(navController = navController)
        }
        composable(Screen.SCREEN3.router){
            Screen3(navController = navController)
        }
        composable(Screen.LOGIN.router){
            LoginScreen(navController = navController)
        }
        composable(Screen.MOVIE_SCREEN.router){
            Bai1(movies = movieState.value)
        }
    }
}

@Composable
fun Screen1(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
            .clickable {
                navController.navigate(Screen.SCREEN2.router) {
                    popUpTo(Screen.SCREEN1.router) {
                        inclusive = true
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text("Screen 1", color = Color.White, fontSize = 20 .sp)
    }
}
@Composable
fun Screen2(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow)
            .clickable { navController.navigate(Screen.SCREEN3.router) },
        contentAlignment = Alignment.Center
    ) {
        Text("Screen 2", color = Color.White, fontSize = 20.sp)
    }
}
@Composable
fun Screen3(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
            .clickable { navController.navigate(Screen.SCREEN1.router) },
        contentAlignment = Alignment.Center
    ) {
        Text("Screen 3", color = Color.White, fontSize = 20.sp)
    }
}