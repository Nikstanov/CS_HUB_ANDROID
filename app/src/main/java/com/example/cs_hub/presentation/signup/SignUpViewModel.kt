package com.example.cs_hub.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs_hub.data.AuthRepository
import com.example.cs_hub.data.models.User
import com.example.cs_hub.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(){
    private val _signUpState = Channel<SignUpState>()
    val signUpState = _signUpState.receiveAsFlow()

    fun registerUser(email:String, password: String, user: User)= viewModelScope.launch{
        authRepository.registerUser(email,password).collect{result ->
            when(result){
                is Resource.Success ->{
                    _signUpState.send(SignUpState(isSuccess = "Sign up success"))
                    user.email = email
                    authRepository.saveUser(user)
                }
                is Resource.Error -> {
                    _signUpState.send(SignUpState(isError = result.message))
                }
                is Resource.Loading ->{
                    _signUpState.send(SignUpState(isLoading = true))
                }
            }
        }
    }
}