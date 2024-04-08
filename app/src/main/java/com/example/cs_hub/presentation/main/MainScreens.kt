package com.example.cs_hub.presentation.main

import com.example.cs_hub.navigation.Screens

sealed class MainScreens (val name: String) {
    data object PlayersScreen : MainScreens(name = "Players")
    data object ProfileScreen : MainScreens(name = "Profile")
}