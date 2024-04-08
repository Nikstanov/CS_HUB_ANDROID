package com.example.cs_hub.data

import android.graphics.Bitmap
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.cs_hub.data.models.Player

interface StorageRepository {

    fun getImages() : SnapshotStateList<Bitmap>
    fun subscribeToPlayerImages(nickname : String)
}