package com.example.cs_hub.presentation.main.players


import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cs_hub.R

import com.example.cs_hub.data.models.Player
import com.example.cs_hub.navigation.Screens
import com.example.cs_hub.presentation.signup.getCountries
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(playerId : Int? = null, navController: NavController, viewModel: PlayersViewModel = hiltViewModel()) {
    val countries by remember {mutableStateOf(Locale.getISOCountries())}
    val countriesNames by remember {mutableStateOf(getCountries())}
    var countryExpanded by remember { mutableStateOf(false) }

    val isNew = playerId == null
    var player  = Player()
    if (playerId != null){
        player = viewModel.getPlayerById(playerId)
    }
    val imageSlider = viewModel.getImages()
    val pagerState = rememberPagerState(initialPage = 0)

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.playerSavingState.collectAsState(initial = null)

    var liked by remember {
        mutableStateOf(viewModel.isLiked(player.nick_name))
    }
    var nicknameError by remember {
        mutableStateOf("")
    }
    var nickname by rememberSaveable {
        mutableStateOf(player.nick_name)
    }

    var fullNameError by remember {
        mutableStateOf("")
    }
    var fullName by rememberSaveable {
        mutableStateOf(player.full_name)
    }

    var nationality by remember {
        mutableStateOf(player.nationality)
    }

    var teamNameError by remember {
        mutableStateOf("")
    }
    var teamName by rememberSaveable {
        mutableStateOf(player.team_name)
    }

    val birthDate by rememberSaveable {
        mutableStateOf(player.birth_date)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.padding(10.dp)){
        Row{
            if(isNew){
                Text("New",fontSize = 10.em, fontWeight = FontWeight.Bold)
            }
            else{
                Text(text = player.nick_name, fontSize = 10.em,fontWeight = FontWeight.Bold)
            }
        }
        if(!isNew){
            if(liked){
                IconButton(onClick = {
                    liked = !liked
                    viewModel.removeLike(player.nick_name)
                }) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "liked",
                    )
                }

            }
            else{
                IconButton(onClick = {
                    liked = !liked
                    viewModel.addLike(player.nick_name)
                }){
                    Icon(
                        Icons.Filled.FavoriteBorder,
                        contentDescription = "not_liked"
                    )
                }

            }
        }


        HorizontalPager(
            count = imageSlider.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .height(114.dp)
                .fillMaxWidth()
        ) { page ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                Image(
                    bitmap = imageSlider[page].asImageBitmap(),
                    contentDescription = "some useful description",
                )
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = Color.White,
            inactiveColor = Color.LightGray,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )

        if(isNew){
            TextField(value = nickname, onValueChange = {
                nicknameError = ""
                nickname = it
                if(nickname.length < 8){
                    nicknameError = "Too low nickname"
                }
            }, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                placeholder = {
                    Text(text = "nickname")
                })
            if(nicknameError != ""){
                Text(text = nicknameError, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        TextField(value = fullName, onValueChange = {
            fullNameError = ""
            fullName = it
            if(fullName.length < 8){
                fullNameError = "Too low full name"
            }
        }, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            placeholder = {
                Text(text = "full name")
            })
        if(fullNameError != ""){
            Text(text = fullNameError, color = Color.Red)
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = teamName, onValueChange = {
            teamNameError = ""
            teamName = it
            if(teamName.length < 3){
                teamNameError = "Too low team name"
            }
        }, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            placeholder = {
                Text(text = "team name")
            })
        if(teamNameError != ""){
            Text(text = teamNameError, color = Color.Red)
        }
        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = countryExpanded,
            onExpandedChange = { countryExpanded = !countryExpanded },
        ) {
            TextField(
                readOnly = true,
                value = nationality,
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
                countries.forEachIndexed { ind,selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = countriesNames[ind]) },
                        onClick = {
                            nationality = selectionOption
                            countryExpanded = false
                        })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        val datePickerState = rememberDatePickerState(birthDate.time)
        val openDialog = remember { mutableStateOf(false) }

        Row{
            Button(onClick = {
                openDialog.value = true
            }) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = "date"
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            TextField(readOnly = true, value = Date(datePickerState.selectedDateMillis!!).toString(), onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                placeholder = {
                    Text(text = "birth date:")
                })
        }


        if(openDialog.value){
            DatePickerDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                confirmButton = {
                    TextButton(onClick = {
                        openDialog.value = false
                    }) {
                        Text(text = "ok")
                    }
                }) {
                DatePicker(state = datePickerState)
            }
        }

        Button(onClick = {
            scope.launch {
                if(nicknameError == "" && fullNameError == "" && teamNameError == ""){
                    viewModel.savePlayer(Player(birth_date = Date(datePickerState.selectedDateMillis!!), full_name = fullName, nationality = nationality, nick_name = nickname, team_name = teamName))
                }
                else{
                    Toast.makeText(context, "Incorrect input values", Toast.LENGTH_LONG).show()
                }
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 30.dp, end = 30.dp), shape = RoundedCornerShape(15.dp)
        ) {
            Text(text = "Save")
        }

        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            if(state.value?.isLoading == true){
                CircularProgressIndicator()
            }
        }

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