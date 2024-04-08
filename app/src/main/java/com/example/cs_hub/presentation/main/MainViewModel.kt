package com.example.cs_hub.presentation.main

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.cs_hub.data.AuthRepository
import com.example.cs_hub.data.PlayersRepository
import com.example.cs_hub.data.StorageRepository
import com.example.cs_hub.data.models.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val storageRepository: StorageRepository
) : ViewModel(){
    fun subscribeToPlayerImages(nickname : String){
        storageRepository.subscribeToPlayerImages(nickname)
    }
}