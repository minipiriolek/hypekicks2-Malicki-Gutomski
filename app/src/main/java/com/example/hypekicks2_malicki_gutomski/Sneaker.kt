package com.example.hypekicks2_malicki_gutomski

import java.io.Serializable

data class Sneaker(
    val brand: String = "",
    val modelName: String = "",
    val releaseYear: Int = 0,
    val resellPrice: Int = 0,
    val imageUrl: String = "",
    var id: String = ""
) : Serializable