package com.ovoskop.appcrafttest.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ovoskop.appcrafttest.R
import com.ovoskop.appcrafttest.utils.adapters.SaveAlbumsAdapter
import com.ovoskop.appcrafttest.utils.app

class SavedAlbumsFragment : Fragment() {

    private lateinit var list: RecyclerView

    private lateinit var adapter: SaveAlbumsAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_saved, container, false)
        list = root.findViewById(R.id.save_albums_list)

        list.layoutManager = LinearLayoutManager(requireContext())
        adapter = SaveAlbumsAdapter(requireContext(), this)
        list.adapter = adapter

        getAlbums()

        return root
    }

    private fun getAlbums() {
        val db = requireContext().app.database
        val albums = db.albumDAO()

        albums.getAll().observe(viewLifecycleOwner) {

            val newList = it.reversed()
            adapter.reload(newList)

        }

    }
}