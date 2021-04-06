package com.ovoskop.appcrafttest.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ovoskop.appcrafttest.R
import com.ovoskop.appcrafttest.network.NetworkController
import com.ovoskop.appcrafttest.network.pojo.Album
import com.ovoskop.appcrafttest.utils.adapters.AlbumsAdapter
import com.ovoskop.appcrafttest.utils.isNetworkConnected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AlbumsFragment : Fragment() {

    private lateinit var list: RecyclerView

    private lateinit var adapter: AlbumsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_albums, container, false)

        list = root.findViewById(R.id.list_albums)
        list.layoutManager = LinearLayoutManager(requireContext())

        if (isNetworkConnected(requireContext())) {
            getAlbums()
        }

        return root
    }

    private fun getAlbums() {
        CoroutineScope(Dispatchers.IO).launch {
            val albums = NetworkController().getAlbums()
            setAlbums(albums)
        }
    }

    private suspend fun setAlbums(albums: List<Album>) = withContext(Dispatchers.Main) {
        adapter = AlbumsAdapter(requireContext(), this@AlbumsFragment, albums)
        list.adapter = adapter
    }
}