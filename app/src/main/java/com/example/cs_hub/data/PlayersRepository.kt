package com.example.cs_hub.data

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.cs_hub.data.models.Player
import com.example.cs_hub.utills.Resource
import kotlinx.coroutines.flow.Flow

interface PlayersRepository {
    fun getPlayerByName(name: String) : Player?
    fun getPlayers() : SnapshotStateList<Player>
    fun savePlayer(player:Player): Flow<Resource<Void>>
}