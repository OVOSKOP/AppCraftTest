package com.ovoskop.appcrafttest.ui.previews

import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
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
import com.ovoskop.appcrafttest.network.pojo.Photo
import com.ovoskop.appcrafttest.utils.*
import com.ovoskop.appcrafttest.utils.adapters.PreviewAdapter
import com.ovoskop.appcrafttest.utils.decorators.PreviewsDecorator
import kotlinx.coroutines.*

class NetworkLoadPreviewsController(private val fragment: Fragment, root: View, private val albumId: Int) {

    private var list: RecyclerView = root.findViewById(R.id.list_previews)
    private var saveAlbum: FloatingActionButton = root.findViewById(R.id.save_album)

    private var progress: CardView = root.findViewById(R.id.progress_load)

    private lateinit var adapter: PreviewAdapter

    private var db: AppDatabase = fragment.requireContext().app.database
    private var albums: AlbumDAO = db.albumDAO()
    private var previews: PhotoDAO = db.photoDAO()

    private var save: Job? = null

    private var isSaving = false

    fun load() {

        albums.getById(albumId).observe(fragment.viewLifecycleOwner) {
            if (it == null) {
                saveAlbum.visibility = View.VISIBLE
            }
        }

        saveAlbum.setOnClickListener {
            it.visibility = View.INVISIBLE
            progress.visibility = View.VISIBLE

            saveAlbum()
        }

        if (isNetworkConnected(fragment.requireContext())) {
            getPreviews()
        }


    }

    private fun getPreviews() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = NetworkController().getPreviews(albumId)
            setList(list)
        }
    }

    private suspend fun setList(listAlbums: List<Photo>) = withContext(Dispatchers.Main) {
        adapter = PreviewAdapter(fragment.requireContext(), fragment, listAlbums, getWidthImage(fragment.requireContext(), 3))
        list.adapter = adapter
    }

    private fun saveAlbum() {
        isSaving = true
        save = CoroutineScope(Dispatchers.IO).launch {
            val album = NetworkController().getAlbum(albumId)
            val photos = NetworkController().getPreviews(albumId)

            for (photo in photos) {

                var url = ""
                var thumbnailUrl = ""
                if (fragment.isAdded) {
                    url = saveImage(fragment.requireContext(), photo.url, "600")
                }
                if (fragment.isAdded) {
                    thumbnailUrl = saveImage(fragment.requireContext(), photo.thumbnailUrl, "150")
                }

                if (fragment.isAdded) {
                    val photoRoom = PhotoRoom(
                            photo.albumId,
                            photo.id,
                            photo.title,
                            url,
                            thumbnailUrl
                    )

                    previews.insert(photoRoom)
                }

            }

            if (fragment.isAdded) {
                val albumRoom = AlbumRoom(album.userId, album.id, album.title)
                albums.insert(albumRoom)
            }

            withContext(Dispatchers.Main) {
                progress.visibility = View.GONE
                saveAlbum.visibility = View.GONE
                isSaving = false
            }
        }
    }

    fun onStop() {

        if (isSaving) {
            save?.cancel()
            previews.getByAlbum(albumId).observeForeverOnce(object : Observer<List<PhotoRoom>> {

                override fun onChanged(listPhotos: List<PhotoRoom>?) {
                    println(listPhotos)
                    if (listPhotos.isNullOrEmpty()) {
                        previews.getByAlbum(albumId).removeObserver(this)
                    } else {

                        for (photo in listPhotos) {
                            deleteFile(fragment.requireContext(), photo.thumbnailUrl, "150")
                            deleteFile(fragment.requireContext(), photo.url, "600")

                            CoroutineScope(Dispatchers.IO).launch {
                                previews.delete(photo)
                            }
                        }

                        previews.getByAlbum(albumId).removeObserver(this)

                    }

                }

            })

            albums.getById(albumId).observeForeverOnce(object : Observer<AlbumRoom> {

                override fun onChanged(albumsList: AlbumRoom?) {
                    val observer = this

                    if (albumsList != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            albums.delete(albumsList)
                        }
                    } else {
                        albums.getById(albumId).removeObserver(observer)
                    }

                }
            })

        }

    }



}