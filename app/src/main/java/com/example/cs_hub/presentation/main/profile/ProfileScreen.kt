package com.example.cs_hub.presentation.main.profile

import android.util.Patterns
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.cs_hub.data.models.User
import com.example.cs_hub.navigation.Screens
import com.example.cs_hub.presentation.signup.SignUpViewModel
import com.example.cs_hub.presentation.signup.getCountries
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
) {

    val user = viewModel.getUser()
    val countries by remember { mutableStateOf(Locale.getISOCountries()) }
    val countriesNames by remember { mutableStateOf(getCountries()) }
    var countryExpanded by remember { mutableStateOf(false) }
    var selectedCountryOption by remember { mutableStateOf(countries[0]) }

    var emailError by remember {
        mutableStateOf("")
    }
    var email by rememberSaveable {
        mutableStateOf(user.email)
    }
    var age by rememberSaveable {
        mutableIntStateOf(user.age)
    }
    var firstname by rememberSaveable {
        mutableStateOf(user.firstname)
    }
    var lastname by rememberSaveable {
        mutableStateOf(user.lastname)
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.userSavingState.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "User", fontSize = 20.em)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Profile", fontSize = 15.em)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = email, onValueChange = {
            emailError = ""
            email = it
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailError = "Incorrect email"
            }
        }, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            placeholder = {
                Text(text = "email")
            })
        if (emailError != "") {
            Text(text = emailError, color = Color.Red)
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = firstname, onValueChange = {
            firstname = it
        }, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            placeholder = {
                Text(text = "firstname")
            })
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = lastname, onValueChange = {
            lastname = it
        }, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            placeholder = {
                Text(text = "lastname")
            })

        Spacer(modifier = Modifier.height(16.dp))
        ExposedDropdownMenuBox(
            expanded = countryExpanded,
            onExpandedChange = { countryExpanded = !countryExpanded },
        ) {
            TextField(
                readOnly = true,
                value = selectedCountryOption,
                onValueChange = { },
                label = { Text("Country") },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
            )
            ExposedDropdownMenu(
                expanded = countryExpanded,
                onDismissRequest = {
                    countryExpanded = false
                }
            ) {
                countries.forEachIndexed { ind, selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = countriesNames[ind]) },
                        onClick = {
                            selectedCountryOption = selectionOption
                            countryExpanded = false
                        })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Age:")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { if (age > 0) age-- },
                    shape = RoundedCornerShape(100)
                ) { Text("-") }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = age.toString(), fontSize = 6.em)
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { if (age < 100) age++ },
                    shape = RoundedCornerShape(100)
                ) { Text("+") }
            }
        }

        Button(
            onClick = {
                scope.launch {
                    if (emailError == "") {
                        viewModel.saveUser(
                            User(
                                age = age,
                                firstname = firstname,
                                lastname = lastname,
                                country = selectedCountryOption,
                                email = email
                            )
                        )
                    } else {
                        Toast.makeText(context, "Incorrect input values", Toast.LENGTH_LONG).show()
                    }
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 30.dp, end = 30.dp), shape = RoundedCornerShape(15.dp)
        ) {
            Text(text = "Save")
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            if (state.value?.isLoading == true) {
                CircularProgressIndicator()
            }
        }

        LaunchedEffect(key1 = state.value?.isSuccess) {
            scope.launch {
                if (state.value?.isSuccess?.isNotEmpty() == true) {
                    val success = state.value?.isSuccess
                    Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                    navController.navigate(Screens.MainScreen.route)
                }

            }
        }

        LaunchedEffect(key1 = state.value?.isError) {
            scope.launch {
                if (state.value?.isError?.isNotEmpty() == true) {
                    val error = state.value?.isError
                    Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                }

            }
        }
    }
}