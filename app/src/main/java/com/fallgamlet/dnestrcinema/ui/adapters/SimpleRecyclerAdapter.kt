package com.fallgamlet.dnestrcinema.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class SimpleRecyclerAdapter(
        protected val itemCountProvider: () -> Int,
        protected val itemViewTypeProvider: (position: Int) -> Int = { it },
        protected val viewHolderFactory: (parent: ViewGroup, viewType: Int) -> SimpleViewHolder
) : RecyclerView.Adapter<SimpleViewHolder>() {

    override fun getItemCount(): Int = itemCountProvider()

    override fun getItemViewType(position: Int): Int = itemViewTypeProvider(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder
            = viewHolderFactory(parent, viewType)

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) = holder.applyData(position)

}
