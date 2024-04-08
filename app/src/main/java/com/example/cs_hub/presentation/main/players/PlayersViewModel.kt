package com.example.cs_hub.presentation.main.players

import android.graphics.Bitmap
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs_hub.data.AuthRepository
import com.example.cs_hub.data.PlayersRepository
import com.example.cs_hub.data.StorageRepository
import com.example.cs_hub.data.models.Player
import com.example.cs_hub.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val playersRepository: PlayersRepository,
    private val storageRepository: StorageRepository
) : ViewModel(){

    private val _playerSavinState = Channel<PlayerSavingState>()
    val playerSavingState = _playerSavinState.receiveAsFlow()

    fun getPlayers() : SnapshotStateList<Player> {
        return playersRepository.getPlayers()
    }
    fun getPlayerById(id : Int):Player {
        return playersRepository.getPlayers()[id]
    }
    fun getImages() : SnapshotStateList<Bitmap>{
        return storageRepository.getImages()
    }
    fun subscribeToPlayerImages(nickname : String){
        storageRepository.subscribeToPlayerImages(nickname)
    }
    fun getLikedPlayers(): SnapshotStateList<String> {
        return authRepository.getLikedPlayers()
    }
    fun addLike(nickname:String){
        authRepository.addLike(nickname)
    }
    fun removeLike(nickname:String){
        authRepository.removeLike(nickname)
    }
    fun isLiked(nickname: String) : Boolean{
        return authRepository.isLiked(nickname)
    }

    fun savePlayer(player : Player) = viewModelScope.launch{
        playersRepository.savePlayer(player).collect{result ->
            when(result){
                is Resource.Success ->{
                    _playerSavinState.send(PlayerSavingState(isSuccess = "Saving success"))
                }
                is Resource.Error -> {
                    _playerSavinState.send(PlayerSavingState(isError = result.message))
                }
                is Resource.Loading ->{
                    _playerSavinState.send(PlayerSavingState(isLoading = true))
                }
            }
        }
    }
}