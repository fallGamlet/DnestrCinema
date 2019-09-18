package com.fallgamlet.dnestrcinema.ui.adapters


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*


open class SimpleRecyclerAdapter<M>(
    private val viewHolderFactory: (ViewGroup, Int) -> ViewHolder<M>,
    private val viewTypeGetter: (Int) -> Int = { 0 },
    private val onItemClickListener: ((M) -> Unit)? = null
) : RecyclerView.Adapter<SimpleRecyclerAdapter.ViewHolder<M>>() {

    private val items: MutableList<M> = mutableListOf()


    override fun getItemViewType(position: Int): Int {
        return viewTypeGetter(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<M> {
        val viewHolder =  viewHolderFactory(parent, viewType)

        if (onItemClickListener != null) {
            viewHolder.itemView.setOnClickListener {
                val item = getItem(viewHolder.adapterPosition)
                onItemClickListener.invoke(item)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<M>, position: Int) {
        val item = getItem(position)
        holder.applyData(item)
    }

    fun getItem(position: Int): M {
        return items[position]
    }

    fun getPosition(item: M): Int {
        return if (items.isEmpty()) -1 else items.indexOf(item)

    }

    fun getItems(): List<M> {
        return ArrayList(items)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun clear() {
        rangeRemove(0, itemCount)
    }

    fun rangeRemove(position: Int, count: Int) {
        if (position < 0 || position >= itemCount || count <= 0) {
            return
        }

        items.subList(position, position + count).clear()
        notifyItemRangeRemoved(position, count)
    }

    fun setData(items: List<M>?) {
        this.items.clear()
        if (items != null) {
            this.items.addAll(items)
        }
        notifyDataSetChanged()
    }

    fun setItem(item: M, position: Int) {
        this.items[position] = item
        notifyItemChanged(position, Any())
    }

    fun add(items: List<M>) {
        add(itemCount, items)
    }

    fun add(position: Int, items: List<M>) {
        if (position < 0 || position > itemCount || items.isEmpty()) {
            return
        }

        this.items.addAll(position, items)
        notifyItemRangeInserted(position, items.size)
    }



    abstract class ViewHolder<M>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun applyData(data: M)
    }

}
