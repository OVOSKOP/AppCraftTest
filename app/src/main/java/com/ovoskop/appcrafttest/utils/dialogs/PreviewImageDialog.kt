package com.ovoskop.appcrafttest.utils.dialogs

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.github.chrisbanes.photoview.PhotoView
import com.github.chrisbanes.photoview.PhotoViewAttacher
import com.ovoskop.appcrafttest.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.io.File
import java.lang.Exception

class PreviewImageDialog : DialogFragment() {

    private lateinit var preview: PhotoView
    private var uriImage: String? = null
    private var fileImage: File? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.dialog_image, container, false)

        preview = root.findViewById(R.id.photo_open)

        if (uriImage != null) {
            Picasso.get()
                .load(uriImage)
                .into(preview)
        }
        if (fileImage != null) {
            Picasso.get()
                .load(fileImage!!)
                .into(preview)
        }

        PhotoViewAttacher(preview)

        return root
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    fun setImageUri(imagePath: String?) {
        uriImage = imagePath
    }

    fun setImageFile(imageFile: File?) {
        fileImage = imageFile
    }

}