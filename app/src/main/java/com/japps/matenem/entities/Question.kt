package com.japps.matenem.entities

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("comando") val comando: String,
    @SerializedName("a") val a: String,
    @SerializedName("b") val b: String,
    @SerializedName("c") val c: String,
    @SerializedName("d") val d: String,
    @SerializedName("e") val e: String,
    @SerializedName("gabarito") val gabarito: String
)
