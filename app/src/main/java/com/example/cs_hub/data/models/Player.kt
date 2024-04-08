package com.example.cs_hub.data.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.Date

@IgnoreExtraProperties
data class Player(
    var birth_date: Date = Date(),
    var full_name: String = "",
    var nationality: String = "",
    var nick_name: String = "",
    var team_name: String = ""
){

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "birth_date" to birth_date,
            "full_name" to full_name,
            "nationality" to nationality,
            "nick_name" to nick_name,
            "team_name" to team_name,
        )
    }
}
