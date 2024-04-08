package com.example.cs_hub.di

import com.example.cs_hub.data.AuthRepository
import com.example.cs_hub.data.AuthRepositoryImpl
import com.example.cs_hub.data.PlayersRepository
import com.example.cs_hub.data.PlayersRepositoryImpl
import com.example.cs_hub.data.StorageRepository
import com.example.cs_hub.data.StorageRepositoryImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseDatabase() = Firebase.firestore

    @Provides
    @Singleton
    fun providesFirebaseStorage() = Firebase.storage.reference

    @Provides
    @Singleton
    fun providesPlayerRepository(firebaseDatabase : FirebaseFirestore): PlayersRepository{
        return PlayersRepositoryImpl(firebaseDatabase)
    }
    @Provides
    @Singleton
    fun providesAuthRepository(firebaseAuth: FirebaseAuth, firebaseDatabase : FirebaseFirestore):AuthRepository{
        return AuthRepositoryImpl(firebaseAuth, firebaseDatabase)
    }
    @Provides
    @Singleton
    fun providesStorageRepository(storageReference : StorageReference):StorageRepository{
        return StorageRepositoryImpl(storageReference)
    }
}