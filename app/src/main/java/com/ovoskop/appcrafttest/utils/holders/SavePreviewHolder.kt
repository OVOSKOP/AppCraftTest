package com.ovoskop.appcrafttest.utils.holders

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ovoskop.appcrafttest.database.entities.PhotoRoom
import com.ovoskop.appcrafttest.utils.dialogs.PreviewImageDialog
import com.ovoskop.appcrafttest.utils.getFileUri
import com.squareup.picasso.Picasso

class SavePreviewHolder(itemView: ImageView, private val fragment: Fragment) : RecyclerView.ViewHolder(itemView) {

    private val imageView = itemView

    fun onBind(photo: PhotoRoom) {

        Picasso.get()
            .load(getFileUri(fragment.requireContext(), photo.thumbnailUrl, "150"))
            .into(imageView)

        imageView.setOnClickListener {
            val file = getFileUri(fragment.requireContext(), photo.thumbnailUrl, "600")

            val dialog = PreviewImageDialog()
            dialog.setImageFile(file)

            dialog.show(fragment.childFragmentManager, "image")
        }

    }

}