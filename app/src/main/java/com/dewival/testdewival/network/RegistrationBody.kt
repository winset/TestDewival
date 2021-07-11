package com.dewival.testdewival.network

import com.google.gson.annotations.SerializedName

class RegistrationBody(
        @SerializedName("login")
        val login:String,
        @SerializedName("password")
        val password:String
)