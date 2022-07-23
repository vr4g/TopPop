package com.example.toppop.models

data class Songs(
    var title: String,
    var duration: Int,
    var position: Int,
    var artist: Artists,
    var album: Album,
)
