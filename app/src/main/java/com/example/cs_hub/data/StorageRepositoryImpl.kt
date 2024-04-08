package com.example.cs_hub.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.cs_hub.data.models.Player
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storageReference: StorageReference
): StorageRepository {

    private val imagesList = mutableStateListOf<Bitmap>()

    override fun getImages(): SnapshotStateList<Bitmap> {
        return imagesList
    }

    override fun subscribeToPlayerImages(nickname: String) {
        imagesList.clear()

        storageReference.child(nickname).listAll().addOnSuccessListener { list ->
            for(item in list.items){
                item.getBytes(1024*1024).addOnSuccessListener {
                    imagesList.add(BitmapFactory.decodeByteArray(it, 0, it.size))
                }
            }
        }
    }
}