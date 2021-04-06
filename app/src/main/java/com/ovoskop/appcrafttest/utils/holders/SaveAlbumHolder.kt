package com.ovoskop.appcrafttest.utils.holders

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ovoskop.appcrafttest.R
import com.ovoskop.appcrafttest.database.entities.AlbumRoom
import com.ovoskop.appcrafttest.utils.app
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaveAlbumHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.findViewById(R.id.title_album)
    private val card: View = itemView.findViewById(R.id.album_card)
    private val delete: ImageView = itemView.findViewById(R.id.delete_album)

    fun onBind(album: AlbumRoom, fragment: Fragment) {
        title.text = album.title

        delete.visibility = View.VISIBLE

        card.setOnClickListener {
            val args = Bundle()
            args.putInt("albumId", album.id ?: -1)
            args.putBoolean("saved", true)

            fragment.findNavController().navigate(R.id.to_photo, args)
        }

        delete.setOnClickListener {
            val db = fragment.requireContext().app.database
            val albums = db.albumDAO()

            CoroutineScope(Dispatchers.IO).launch {
                albums.delete(album)
            }
        }
    }

}