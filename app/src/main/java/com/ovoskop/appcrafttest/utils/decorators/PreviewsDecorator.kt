package com.ovoskop.appcrafttest.utils.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PreviewsDecorator(private val margin: Int, private val countSpan: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val position  = parent.getChildLayoutPosition(view)

        outRect.right = margin / 2
        outRect.bottom = margin
        outRect.left = margin / 2

        if (position < countSpan) {
            outRect.top = margin
        }

        if (position % countSpan == 0) {
            outRect.left = margin
        }

        if (position % countSpan == 1) {
            outRect.left = margin / 2 + 5
        }

        if (position % countSpan == 2) {
            outRect.left = margin / 2 - 5
        }

    }

}