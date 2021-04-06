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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
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
import com.ovoskop.appcrafttest.utils.*
import com.ovoskop.appcrafttest.utils.adapters.AlbumsAdapter
import com.ovoskop.appcrafttest.utils.adapters.PreviewAdapter
import com.ovoskop.appcrafttest.utils.adapters.SavePreviewAdapter
import com.ovoskop.appcrafttest.utils.decorators.PreviewsDecorator
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PreviewsFragment : Fragment() {

    private lateinit var list: RecyclerView

    private lateinit var db: AppDatabase
    private lateinit var previews: PhotoDAO

    private var controller: NetworkLoadPreviewsController? = null

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

        list = root.findViewById(R.id.list_previews)
        list.layoutManager = GridLayoutManager(requireContext(), 3)
        list.addItemDecoration(
                PreviewsDecorator(
                        resources.getDimensionPixelSize(R.dimen.previews_margin),
                        3
                )
        )

        db = requireContext().app.database
        previews = db.photoDAO()

        if (!saved) {
            controller = NetworkLoadPreviewsController(this, root, albumId)
            controller?.load()
        } else {
            getSavedPreviews()
        }

        return root
    }

    override fun onStop() {
        controller?.onStop()

        super.onStop()
    }

    private fun getSavedPreviews() {
        previews.getByAlbum(albumId).observe(viewLifecycleOwner) {
            val savedAdapter = SavePreviewAdapter(requireContext(), this@PreviewsFragment, it, getWidthImage(requireContext(), 3))

            list.adapter = savedAdapter
        }
    }

}
