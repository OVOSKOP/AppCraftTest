package com.ovoskop.appcrafttest.ui.previews

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ovoskop.appcrafttest.R
import com.ovoskop.appcrafttest.database.AppDatabase
import com.ovoskop.appcrafttest.database.dao.AlbumDAO
import com.ovoskop.appcrafttest.database.dao.PhotoDAO
import com.ovoskop.appcrafttest.database.entities.AlbumRoom
import com.ovoskop.appcrafttest.database.entities.PhotoRoom
import com.ovoskop.appcrafttest.network.NetworkController
import com.ovoskop.appcrafttest.network.pojo.Album
import com.ovoskop.appcrafttest.network.pojo.Photo
import com.ovoskop.appcrafttest.network.services.AlbumsService
import com.ovoskop.appcrafttest.network.services.PreviewsService
import com.ovoskop.appcrafttest.utils.BASE_URL
import com.ovoskop.appcrafttest.utils.adapters.AlbumsAdapter
import com.ovoskop.appcrafttest.utils.adapters.PreviewAdapter
import com.ovoskop.appcrafttest.utils.adapters.SavePreviewAdapter
import com.ovoskop.appcrafttest.utils.app
import com.ovoskop.appcrafttest.utils.decorators.PreviewsDecorator
import com.ovoskop.appcrafttest.utils.getWidthImage
import com.ovoskop.appcrafttest.utils.saveImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PreviewsFragment : Fragment() {

    private lateinit var list: RecyclerView
    private lateinit var saveAlbum: FloatingActionButton
    private lateinit var progress: CardView

    private lateinit var adapter: PreviewAdapter

    private lateinit var db: AppDatabase
    private lateinit var albums: AlbumDAO
    private lateinit var previews: PhotoDAO

    private var albumId: Int = -1
    private var saved = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_previews, container, false)

        albumId = requireArguments().getInt("albumId", -1)
        saved = requireArguments().getBoolean("saved", false)

        db = requireContext().app.database
        albums = db.albumDAO()
        previews = db.photoDAO()

        if (!saved) {
            albums.getById(albumId).observe(viewLifecycleOwner) {
                if (it == null) {
                    saveAlbum.visibility = View.VISIBLE
                }
            }
        }

        saveAlbum = root.findViewById(R.id.save_album)
        progress = root.findViewById(R.id.progress_load)

        list = root.findViewById(R.id.list_previews)
        list.layoutManager = GridLayoutManager(requireContext(), 3)
        list.addItemDecoration(
            PreviewsDecorator(
                resources.getDimensionPixelSize(R.dimen.previews_margin),
                3
            )
        )

        saveAlbum.setOnClickListener {
            it.visibility = View.INVISIBLE
            progress.visibility = View.VISIBLE

            saveAlbum()
        }

        if (!saved) {
            getPreviews()
        } else {
            getSavedPreviews()
        }

        return root
    }

    private fun getPreviews() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = NetworkController().getPreviews(albumId)
            setList(list)
        }
    }

    private fun getSavedPreviews() {
        previews.getByAlbum(albumId).observe(viewLifecycleOwner) {
            val savedAdapter = SavePreviewAdapter(requireContext(), this@PreviewsFragment, it, getWidthImage(requireContext(), 3))

            list.adapter = savedAdapter
        }
    }

    private suspend fun setList(listAlbums: List<Photo>) = withContext(Dispatchers.Main) {
        adapter = PreviewAdapter(requireContext(), this@PreviewsFragment, listAlbums, getWidthImage(requireContext(), 3))
        list.adapter = adapter
    }

    private fun saveAlbum() {
        CoroutineScope(Dispatchers.IO).launch {
            val album = NetworkController().getAlbum(albumId)
            val photos = NetworkController().getPreviews(albumId)

            for (photo in photos) {
                val url = saveImage(requireContext(), photo.url, "600")
                val thumbnailUrl = saveImage(requireContext(), photo.thumbnailUrl, "150")

                val photoRoom = PhotoRoom(
                    photo.albumId,
                    photo.id,
                    photo.title,
                    url,
                    thumbnailUrl
                )

                previews.insert(photoRoom)
            }

            val albumRoom = AlbumRoom(album.userId, album.id, album.title)
            albums.insert(albumRoom)

            withContext(Dispatchers.Main) {
                progress.visibility = View.GONE
                saveAlbum.visibility = View.GONE
            }

        }
    }

}