package com.example.cs_hub.presentation.main.players

data class PlayerSavingState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)