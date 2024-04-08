package com.example.cs_hub.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.cs_hub.data.models.Player
import com.example.cs_hub.data.models.User
import com.example.cs_hub.utills.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class PlayersRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseFirestore
): PlayersRepository {

    private val playersList = mutableStateListOf<Player>()

    private var childEventListener : ListenerRegistration? = null

    init {
        Log.d("init", "init firebase listeners")
        childEventListener = firebaseDatabase.collection("players").addSnapshotListener{snapshots, e ->
            if (e != null) {
                Log.w(TAG, "listen:error", e)
            }

            for (dc in snapshots!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED ->{
                        val player = dc.document.toObject<Player>()
                        playersList.add(player)
                        Log.d("add player", player.toString())
                    }
                    DocumentChange.Type.MODIFIED -> {
                        val nickname = dc.document.id
                        playersList.removeIf { it.nick_name == nickname }
                    }
                    DocumentChange.Type.REMOVED -> {
                        val player = dc.document.toObject<Player>()
                        val nickname = dc.document.id
                        playersList.replaceAll {
                            if(it.nick_name == nickname){
                                return@replaceAll player
                            }
                            else{
                                return@replaceAll it
                            }
                        }
                        Log.d("change player", player.toString())
                    }
                }
            }
        }
    }

    override fun getPlayerByName(name: String): Player? {
        return playersList.firstOrNull { it.nick_name == name }
    }

    override fun getPlayers(): SnapshotStateList<Player> {
        return playersList
    }

    override fun savePlayer(player: Player): Flow<Resource<Void>> {

        return flow {
            emit(Resource.Loading())
            val result = firebaseDatabase.collection("players").document(player.nick_name).set(player.toMap()).await()
            Log.d("update player", player.toString())
            emit(Resource.Success(result))
        }
            .catch {
                emit(Resource.Error(it.message.toString()))
            }

    }
}