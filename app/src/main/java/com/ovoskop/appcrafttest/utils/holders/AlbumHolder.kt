package com.ovoskop.appcrafttest.utils.holders

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ovoskop.appcrafttest.R
import com.ovoskop.appcrafttest.network.pojo.Album

class AlbumHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.findViewById(R.id.title_album)
    private val card: View = itemView.findViewById(R.id.album_card)

    fun onBind(album: Album, fragment: Fragment) {
        title.text = album.title

        card.setOnClickListener {
            val args = Bundle()
            args.putInt("albumId", album.id ?: -1)

            fragment.findNavController().navigate(R.id.to_photo, args)
        }
    }

}