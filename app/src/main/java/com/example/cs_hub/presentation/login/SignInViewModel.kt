package com.example.cs_hub.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs_hub.data.AuthRepository
import com.example.cs_hub.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(){
    private val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()

    fun loginUser(email:String, password: String)= viewModelScope.launch{
        authRepository.loginUser(email,password).collect{result ->
            when(result){
                is Resource.Success ->{
                    _signInState.send(SignInState(isSuccess = "Sign in success"))
                }
                is Resource.Error -> {
                    _signInState.send(SignInState(isError = result.message))
                }
                is Resource.Loading ->{
                    _signInState.send(SignInState(isLoading = true))
                }
            }
        }
    }
}