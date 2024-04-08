package com.example.cs_hub.data

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.cs_hub.data.models.Player
import com.example.cs_hub.data.models.User
import com.example.cs_hub.utills.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun getCurrentUser(): User?
    fun getLikedPlayers(): SnapshotStateList<String>
    fun addLike(nickname : String)
    fun removeLike(nickname: String)
    fun isLiked(nickname: String) : Boolean
    fun saveUser(newUser: User)
    fun saveUserAsync(newUser: User): Flow<Resource<Void>>
}