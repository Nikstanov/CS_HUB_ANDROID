package com.example.cs_hub.presentation.main

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cs_hub.R
import com.example.cs_hub.navigation.Screens
import com.example.cs_hub.presentation.main.players.PlayerScreen
import com.example.cs_hub.presentation.main.players.PlayersScreen
import com.example.cs_hub.presentation.main.profile.ProfileScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavController
){
    var openedMenuSelector by rememberSaveable {
        mutableStateOf("Players")
    }

    Scaffold(
        topBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "Sign out", fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.clickable {
                    Firebase.auth.signOut()
                    navController.navigate(Screens.SignUpScreen.route)
                })
                Button(onClick = {
                    viewModel.subscribeToPlayerImages("new")
                    navController.navigate(Screens.NewPlayerScreen.route)
                }) {
                    Icon(
                        painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = "plus"
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigation{
                openedMenuSelector = it
            }
        },
    ) {
        Column(Modifier.padding(it)) {
            when (openedMenuSelector) {
                BottomNavItem.Players.title -> {
                    PlayersScreen(navController = navController)
                }

                BottomNavItem.Profile.title -> {
                    ProfileScreen(navController = navController)
                }

                else -> {
                    PlayersScreen(navController = navController)
                }
            }
        }

    }


}

sealed class BottomNavItem(
    var title: String,
    var icon: Int
) {
    data object Players :
        BottomNavItem(
            "Players",
            R.drawable.baseline_format_list_bulleted_24
        )

    data object Profile :
        BottomNavItem(
            "Profile",
            R.drawable.baseline_account_circle_24
        )
}

@Composable
fun BottomNavigation(
    onSelectionChanged : (String) -> Unit
) {

    val items = listOf(
        BottomNavItem.Players,
        BottomNavItem.Profile
    )

    NavigationBar {
        items.forEach { item ->
            AddItem(
                screen = item,
                onSelectionChanged = onSelectionChanged
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavItem,
    onSelectionChanged : (String) -> Unit
) {
    NavigationBarItem(
        // Text that shows bellow the icon
        label = {
            Text(text = screen.title)
        },

        // The icon resource
        icon = {
            Icon(
                painterResource(id = screen.icon),
                contentDescription = screen.title
            )
        },

        // Display if the icon it is select or not
        selected = true,

        // Always show the label bellow the icon or not
        alwaysShowLabel = true,

        // Click listener for the icon
        onClick = {onSelectionChanged(screen.title)},

        // Control all the colors of the icon
        colors = NavigationBarItemDefaults.colors()
    )
}