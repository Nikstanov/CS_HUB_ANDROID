package com.example.cs_hub.presentation.main.profile

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs_hub.data.AuthRepository
import com.example.cs_hub.data.PlayersRepository
import com.example.cs_hub.data.models.Player
import com.example.cs_hub.data.models.User
import com.example.cs_hub.presentation.signup.SignUpState
import com.example.cs_hub.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(){
    private val _userSavingState = Channel<UserSavingState>()
    val userSavingState = _userSavingState.receiveAsFlow()

    fun saveUser(user: User)= viewModelScope.launch{
        authRepository.saveUserAsync(user).collect{result ->
            when(result){
                is Resource.Success ->{
                    _userSavingState.send(UserSavingState(isSuccess = "Saving success"))
                }
                is Resource.Error -> {
                    _userSavingState.send(UserSavingState(isError = result.message))
                }
                is Resource.Loading ->{
                    _userSavingState.send(UserSavingState(isLoading = true))
                }
            }
        }
    }

    fun getUser() : User {
        return authRepository.getCurrentUser()?:User()
    }
}