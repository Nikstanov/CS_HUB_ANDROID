package com.example.cs_hub.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cs_hub.presentation.signup.SignUpScreen
import com.example.cs_hub.presentation.login.SignInScreen
import com.example.cs_hub.presentation.main.MainScreen
import com.example.cs_hub.presentation.main.players.PlayerScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
) {
    var startDestination = Screens.SignUpScreen.route
    if(Firebase.auth.currentUser != null){
       startDestination = Screens.MainScreen.route
    }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screens.SignInScreen.route) {
            SignInScreen(navController = navController)
        }
        composable(route = Screens.SignUpScreen.route) {
            SignUpScreen(navController = navController)
        }
        composable(route = Screens.MainScreen.route){
            MainScreen(navController = navController)
        }
        composable(route = Screens.NewPlayerScreen.route){
            PlayerScreen(navController = navController)
        }
    }

}