import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.toppop.activities.Details
import com.example.toppop.R
import com.example.toppop.models.Songs


class MyAdapter(private val data: List<Songs>?) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bind(property: Songs) {
            val title = view.findViewById<TextView>(R.id.songTitle)
            val position = view.findViewById<TextView>(R.id.albumSong)
            val name = view.findViewById<TextView>(R.id.artistName)
            val duration = view.findViewById<TextView>(R.id.songDuration)

            title.text = property.title
            position.text = property.position.toString()
            name.text = property.artist.name

            val seconds = property.duration % 60
            val minutes = property.duration / 60
            duration.text = String.format("%02d:%02d", minutes, seconds)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_design, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data?.get(position)!!)
        val context = holder.view.context

        holder.view.setOnClickListener {
            val intent = Intent(context, Details::class.java)
            val extras = Bundle().apply {
                putString("name", data[position].artist.name)
                putString("song", data[position].title)
                putString("album", data[position].album.title)
                putString("image", data[position].album.cover_big)
                putInt("albumId", data[position].album.id)
            }
            intent.putExtras(extras)
            context.startActivity(intent)
        }
    }


}
