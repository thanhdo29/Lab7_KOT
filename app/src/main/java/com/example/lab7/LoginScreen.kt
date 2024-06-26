package com.example.lab7

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController){
    val loginViewModel:LoginViewModel= viewModel()
    LoginCard(navController =navController , loginViewModel = loginViewModel)
}

@Composable
fun LoginCard(navController: NavController, loginViewModel: LoginViewModel){
    val snackbarHostState= remember { SnackbarHostState() }
    HandleLoginState(
        snackbarHostState = snackbarHostState,
        loginViewModel = loginViewModel,
        navController = navController
    )

    Scaffold(snackbarHost={ SnackbarHost(snackbarHostState)}) {
        paddingValues ->
        LoginForm(loginViewModel = loginViewModel, paddingValues = paddingValues)
    }
}

@Composable
fun HandleLoginState(
    snackbarHostState: SnackbarHostState,
    loginViewModel: LoginViewModel,
    navController: NavController
){
    val isAuthenticated by loginViewModel.isAuthenticated.observeAsState()
    
    LaunchedEffect(key1 = isAuthenticated) {
        when(isAuthenticated){
            true->{
                navController.navigate(Screen.MOVIE_SCREEN.router){
                    popUpTo(Screen.LOGIN.router){
                        inclusive=true
                    }
                }
            }

            false->{
                snackbarHostState.showSnackbar(
                    message = "Invalid username or password.",
                    duration = SnackbarDuration.Short
                )
                loginViewModel.resetAuthenticationState()
            }
            null->{}
        }
    }
}

@Composable
fun LoginForm(loginViewModel: LoginViewModel, paddingValues: PaddingValues) {
    val usernameState by loginViewModel.username.observeAsState("")
    val passwordState by loginViewModel.password.observeAsState("")
    val rememberMeState by loginViewModel.rememberMe.observeAsState(false)

    var username by remember { mutableStateOf(usernameState) }
    var password by remember { mutableStateOf(passwordState) }
    var rememberMe by remember { mutableStateOf(rememberMeState) }
    val isLoginEnabled = username.isNotBlank() && password.isNotBlank()

    LaunchedEffect(usernameState, passwordState, rememberMeState) {
        if (usernameState != username) {
            username = usernameState
        }
        if (passwordState != password) {
            password = passwordState
        }
        if (rememberMeState != rememberMe) {
            rememberMe = rememberMeState
        }
        Log.d("PAM", "LoginForm: username $usernameState password $passwordState rememberMe $rememberMeState")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(paddingValues),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(36.dp, 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo",
                )
                Spacer(modifier = Modifier.height(20.dp))
                UsernameField(username) { newUsername ->
                    username = newUsername
                    loginViewModel.updateUsername(newUsername)
                    Log.d("PAM", "UsernameField: $username")
                }
                PasswordField(password) { newPassword ->
                    password = newPassword
                    loginViewModel.updatePassword(newPassword)
                    Log.d("PAM", "PasswordField: $password")
                }
                RememberMeSwitch(rememberMe) { isChecked ->
                    rememberMe = isChecked
                    loginViewModel.updateRememberMe(isChecked)
                    Log.d("PAM", "RememberMeSwitch: $rememberMe")
                }
                Spacer(modifier = Modifier.height(16.dp))
                LoginButton(isLoginEnabled) {
                    loginViewModel.login(
                        username,
                        password,
                        rememberMe
                    )
                }
            }
        }
    }
}


@Composable
fun UsernameField(username:String, onChangeUsername:(String)->Unit){
    OutlinedTextField(value = username,
        onValueChange = onChangeUsername,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Username")},)
}

@Composable
fun PasswordField(password:String, onChangePassword: (String)->Unit){
    OutlinedTextField(value = password,
        onValueChange = onChangePassword,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Password")},
        visualTransformation = PasswordVisualTransformation())
}


@Composable
fun LoginButton(isEnable:Boolean, onLoginClick:()-> Unit){
    Button(onClick = onLoginClick,
        enabled = isEnable,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isEnable) Color.DarkGray else Color.LightGray,
            contentColor = Color.White
        )) {
        Text(text = "Login", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RememberMeSwitch(rememberMe:Boolean, onCheckedChange:(Boolean)->Unit){
    var isChecked by remember {
        mutableStateOf(rememberMe)
    }

    Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
        Switch(checked = isChecked, onCheckedChange = {
            isChecked=it
            onCheckedChange(it)
        }, modifier = Modifier
            .scale(0.75f)
            .padding(0.dp))
        Text("Remember Me?", modifier = Modifier.padding(start = 12.dp))
    }
}