package com.example.cs_hub.data.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var userId : String = "",
    var age:Int = 0,
    var country: String = "",
    var email: String = "",
    var firstname: String = "",
    var lastname:String = ""
){
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "age" to age,
            "country" to country,
            "email" to email,
            "firstname" to firstname,
            "lastname" to lastname,
        )
    }
}
