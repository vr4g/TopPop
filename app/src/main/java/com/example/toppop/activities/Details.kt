package com.example.toppop.activities

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.toppop.R
import com.example.toppop.RetrofitHelper
import com.example.toppop.api.AlbumApi
import com.squareup.picasso.Picasso


class Details : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val artistName = findViewById<TextView>(R.id.detailsName)
        val songName = findViewById<TextView>(R.id.detailsSong)
        val albumName = findViewById<TextView>(R.id.detailsAlbum)
        val coverImage = findViewById<ImageView>(R.id.imageView)
        val coverImageUrl: String?

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        val albumId = extras?.getInt("albumId")

        artistName.text = extras?.getString("name")
        songName.text = extras?.getString("song")
        albumName.text = extras?.getString("album")
        coverImageUrl = extras?.getString("image")
        Picasso.get().load(coverImageUrl).into(coverImage)

        getAlbumSongs(albumId)
    }

    fun getAlbumSongs(id: Int?) {

        val retrofit = RetrofitHelper.getInstance()
        val apiInterface = retrofit.create(AlbumApi::class.java)

        val albumSongs = findViewById<TextView>(R.id.detailsAlbumSongs)
        albumSongs.text = ""

        lifecycleScope.launchWhenCreated {
            try {
                val response = apiInterface.getAlbumData(id)
                if (response.isSuccessful) {
                    for (song in response.body()?.tracks?.data!!) {
                        albumSongs.append("${song.title} \n")
                    }
                }
            } catch (Ex: Exception) {
                Log.e("Error", Ex.localizedMessage as String)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
