package com.ovoskop.appcrafttest.utils.holders

import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ovoskop.appcrafttest.network.pojo.Photo
import com.ovoskop.appcrafttest.utils.dialogs.PreviewImageDialog
import com.squareup.picasso.Picasso

class PreviewHolder(itemView: ImageView, private val fragment: Fragment) : RecyclerView.ViewHolder(itemView) {

    private val imageView = itemView

    fun onBind(photo: Photo) {

        Picasso.get()
            .load(photo.thumbnailUrl)
            .into(imageView)

        imageView.setOnClickListener {
            val dialog = PreviewImageDialog()
            dialog.setImageUri(photo.url)

            dialog.show(fragment.childFragmentManager, "image")
        }

    }

}