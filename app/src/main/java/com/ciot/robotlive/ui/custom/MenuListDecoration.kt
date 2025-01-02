package com.ciot.robotlive.ui.custom

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by p'c on 2025/1/2.
 * Description:
 * Encoding: utf-8
 */
class MenuListDecoration(private val verticalSpaceHeight: Int, private val horizontalSpaceWidth: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        if (position != 0) { // Skip the first item
            outRect.left = 0
        } else {
            outRect.left = horizontalSpaceWidth
        }
        outRect.right = horizontalSpaceWidth
    }
}