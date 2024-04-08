package com.example.cs_hub.presentation.main.players

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cs_hub.data.models.Player
import com.example.cs_hub.navigation.Screens
import com.example.cs_hub.presentation.main.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun PlayersScreen(
    viewModel: PlayersViewModel = hiltViewModel(),
    navController: NavController
){
    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = "players"){
        composable("players"){
            PlayersList(navHostController = navHostController, viewModel)
        }
        composable("players/{playerId}", arguments = listOf(
            navArgument("playerId"){
                type = NavType.IntType
            }
        )){
            val arguments = requireNotNull(it.arguments)
            PlayerScreen(
                playerId = arguments.getInt("playerId"),
                navController = navController
            )
        }
    }
}

@Composable
fun PlayersList(navHostController : NavHostController, viewModel: PlayersViewModel = hiltViewModel()){
    val players = viewModel.getPlayers()
    val likedPlayers = viewModel.getLikedPlayers()
    val likedState = remember { mutableStateOf(false) }
    var searchState by rememberSaveable { mutableStateOf("") }
    Column {
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(20.dp)){
            TextField(value = searchState, onValueChange = {searchState = it},
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(300.dp),
                singleLine = true,
                placeholder = {Text("search")})
            Text("Liked")
            Checkbox(
                checked = likedState.value,
                onCheckedChange = { likedState.value = it }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize(),) {
            itemsIndexed(players){ind, player ->
                if(likedState.value){
                    if(!likedPlayers.contains(player.nick_name)){
                        return@itemsIndexed
                    }
                }
                if(searchState != ""){
                    if(!player.nick_name.contains(searchState)){
                        return@itemsIndexed
                    }
                }
                Row(
                    Modifier
                        .clickable {
                            viewModel.subscribeToPlayerImages(player.nick_name)
                            navHostController.navigate("players/$ind")
                        }
                        .padding(10.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                    Text(text = player.nick_name, color = Color.White, fontSize = 6.em)
                }
                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }
}
