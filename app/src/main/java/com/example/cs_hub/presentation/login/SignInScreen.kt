package com.example.cs_hub.presentation.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cs_hub.navigation.Screens
import kotlinx.coroutines.launch


@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    navController: NavController
) {
    var emailError by remember {
        mutableStateOf("")
    }
    var email by rememberSaveable {
        mutableStateOf("")
    }
    var passwordError by remember {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signInState.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "CS_HUB", fontSize = 20.em)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Sign In", fontSize = 15.em)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = email, onValueChange = {
            emailError = ""
            email = it
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailError = "Incorrect email"
            }
        }, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            placeholder = {
                Text(text = "email")
            })
        if(emailError != ""){
            Text(text = emailError, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = password, onValueChange = {
            passwordError = ""
            password = it
            if(password.length < 8 || password.length > 20){
                passwordError = "password length should be more than 8 and less than 20"
            }
        }, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            placeholder = {
                Text(text = "password")
            })
        if(passwordError != ""){
            Text(text = passwordError, color = Color.Red)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            scope.launch {
                if(emailError == "" && passwordError == ""){
                    viewModel.loginUser(email, password)
                }
                else{
                    Toast.makeText(context, "Incorrect input values", Toast.LENGTH_LONG).show()
                }
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 30.dp, end = 30.dp), shape = RoundedCornerShape(15.dp)
        ) {
            Text(text = "Sign in")
        }

        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            if(state.value?.isLoading == true){
                CircularProgressIndicator()
            }
        }
        Text(text = "Don't have account?", fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.clickable {
            navController.navigate(Screens.SignUpScreen.route)
        })
        
        LaunchedEffect(key1 = state.value?.isSuccess) {
            scope.launch{
                if(state.value?.isSuccess?.isNotEmpty() == true){
                    val success = state.value?.isSuccess
                    Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                    navController.navigate(Screens.MainScreen.route)
                }

            }
        }

        LaunchedEffect(key1 = state.value?.isError) {
            scope.launch{
                if(state.value?.isError?.isNotEmpty() == true){
                    val error = state.value?.isError
                    Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                }

            }
        }
    }
}