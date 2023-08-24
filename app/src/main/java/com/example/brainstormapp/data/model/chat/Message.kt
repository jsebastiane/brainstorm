package com.example.brainstormapp.data.model.chat


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Message(
    @Expose
    @SerializedName("role")
    val role: String,
    @Expose
    @SerializedName("content")
    val content: String,
    val added: Boolean = false
)