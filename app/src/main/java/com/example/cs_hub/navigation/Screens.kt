package com.example.cs_hub.navigation

sealed class Screens(val route: String) {
    data object SignInScreen : Screens(route = "SignIn_Screen")
    data object SignUpScreen : Screens(route = "SignUp_Screen")
    data object MainScreen: Screens(route = "Main_Screen")
    data object NewPlayerScreen: Screens(route = "NewPlayer_Screen")
}