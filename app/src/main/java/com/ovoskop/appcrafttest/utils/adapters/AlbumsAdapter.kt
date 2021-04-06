package com.ovoskop.appcrafttest.utils.adapters

import androidx.fragment.app.Fragment
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ovoskop.appcrafttest.R
import com.ovoskop.appcrafttest.network.pojo.Album
import com.ovoskop.appcrafttest.utils.holders.AlbumHolder

class AlbumsAdapter(private val context: Context, private val fragment: Fragment, private val list: List<Album>) : RecyclerView.Adapter<AlbumHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumHolder {
        return AlbumHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_list_albums, parent, false))
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        holder.onBind(list[position], fragment)
    }

    override fun getItemCount(): Int = list.size
}