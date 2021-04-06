package com.ovoskop.appcrafttest.utils.adapters

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ovoskop.appcrafttest.database.entities.PhotoRoom
import com.ovoskop.appcrafttest.network.pojo.Photo
import com.ovoskop.appcrafttest.utils.holders.PreviewHolder
import com.ovoskop.appcrafttest.utils.holders.SavePreviewHolder

class SavePreviewAdapter(
    private val context: Context,
    private val fragment: Fragment,
    private val list: List<PhotoRoom>,
    private val widthImage: Int
) : RecyclerView.Adapter<SavePreviewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavePreviewHolder {
        val image = ImageView(context)
        image.scaleType = ImageView.ScaleType.CENTER_CROP
        val params = LinearLayout.LayoutParams(widthImage, widthImage)

        image.layoutParams = params

        return SavePreviewHolder(image, fragment)
    }

    override fun onBindViewHolder(holder: SavePreviewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}