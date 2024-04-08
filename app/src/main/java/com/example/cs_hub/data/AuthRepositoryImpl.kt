package com.example.cs_hub.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.cs_hub.data.models.User
import com.example.cs_hub.utills.Resource
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val TAG = "data"

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseFirestore
) : AuthRepository {
    private var user : User? = null
    private var likedPlayers = mutableStateListOf<String>()

    private var userListener : ListenerRegistration? = null
    private var likesEventListener : ListenerRegistration? = null

    init {
        Firebase.auth.addAuthStateListener {
            likedPlayers.clear()
            userListener?.remove()
            likesEventListener?.remove()
            if(it.currentUser != null){
                userListener = firebaseDatabase.collection("users").document(it.currentUser!!.uid).addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error)
                    }

                    if (value != null && value.exists()) {
                        user = value.toObject<User>()
                        user!!.userId = value.id
                    }
                }
                likesEventListener = firebaseDatabase.collection("users").document(it.currentUser!!.uid).collection("liked_players").addSnapshotListener{ snapshots, e ->
                    if (e != null) {
                        Log.w(TAG, "listen:error", e)
                    }

                    for (dc in snapshots!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED ->{
                                val name = dc.document.id
                                likedPlayers.add(name)
                            }
                            DocumentChange.Type.MODIFIED -> {

                            }
                            DocumentChange.Type.REMOVED -> {
                                val name = dc.document.id
                                likedPlayers.remove(name)
                            }
                        }
                    }
                }
            }
        }

    }

    override fun loginUser(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }
            .catch {
                emit(Resource.Error(it.message.toString()))
            }
    }

    override fun registerUser(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            user = User(userId = result.user!!.uid)
            firebaseDatabase.collection("users").document(result.user!!.uid).set(user!!.toMap())
            Log.d("create user", result.user.toString())
            emit(Resource.Success(result))
        }
            .catch {
                emit(Resource.Error(it.message.toString()))
            }
    }

    override fun getCurrentUser(): User? {
        return user
    }

    override fun getLikedPlayers(): SnapshotStateList<String> {
        return likedPlayers
    }

    override fun addLike(nickname: String) {
        firebaseDatabase.collection("users").document(user!!.userId).collection("liked_players").document(nickname).set(emptyMap<String, Any>())
    }

    override fun removeLike(nickname: String) {
        firebaseDatabase.collection("users").document(user!!.userId).collection("liked_players").document(nickname).delete()
    }

    override fun isLiked(nickname: String): Boolean {
        return likedPlayers.contains(nickname)
    }

    override fun saveUser(newUser: User) {
        firebaseDatabase.collection("users").document(user!!.userId).set(newUser.toMap())
    }

    override fun saveUserAsync(newUser: User): Flow<Resource<Void>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseDatabase.collection("users").document(user!!.userId).set(newUser.toMap()).await()
            Log.d("update user", result.toString())
            emit(Resource.Success(result))
        }
            .catch {
                emit(Resource.Error(it.message.toString()))
            }
    }
}