package com.ovoskop.appcrafttest.utils.adapters

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ovoskop.appcrafttest.R
import com.ovoskop.appcrafttest.network.pojo.Photo
import com.ovoskop.appcrafttest.utils.holders.PreviewHolder

class PreviewAdapter(
    private val context: Context,
    private val fragment: Fragment,
    private val list: List<Photo>,
    private val widthImage: Int
) : RecyclerView.Adapter<PreviewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewHolder {
        val image = ImageView(context)
        image.scaleType = ImageView.ScaleType.CENTER_CROP
        val params = LinearLayout.LayoutParams(widthImage, widthImage)

        image.layoutParams = params

        return PreviewHolder(image, fragment)
    }

    override fun onBindViewHolder(holder: PreviewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}