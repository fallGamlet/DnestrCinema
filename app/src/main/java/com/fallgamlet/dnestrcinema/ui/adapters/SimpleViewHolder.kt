package com.fallgamlet.dnestrcinema.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var dataPosition: Int = -1
        private set

    open fun applyData(position: Int) {
        dataPosition = position
    }

}
