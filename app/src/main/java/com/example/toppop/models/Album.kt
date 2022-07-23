package com.example.toppop.models

data class Album(
    var id: Int,
    var title: String,
    var cover_big: String,
    var nb_tracks: Int,
    var tracklist: String,
    var tracks: DataAlbum,
)
