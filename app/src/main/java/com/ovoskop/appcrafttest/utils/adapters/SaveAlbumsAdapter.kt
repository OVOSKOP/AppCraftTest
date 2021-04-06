package com.ovoskop.appcrafttest.utils.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ovoskop.appcrafttest.R
import com.ovoskop.appcrafttest.database.entities.AlbumRoom
import com.ovoskop.appcrafttest.utils.holders.SaveAlbumHolder

class SaveAlbumsAdapter(private val context: Context, private val fragment: Fragment) : RecyclerView.Adapter<SaveAlbumHolder>() {
    private var list: List<AlbumRoom> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveAlbumHolder {
        return SaveAlbumHolder(
            LayoutInflater.from(context)
            .inflate(R.layout.item_list_albums, parent, false))
    }

    override fun onBindViewHolder(holder: SaveAlbumHolder, position: Int) {
        holder.onBind(list[position], fragment)
    }

    override fun getItemCount(): Int = list.size

    fun reload(list: List<AlbumRoom>) {
        this.list = list
        notifyDataSetChanged()
    }
}