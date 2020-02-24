package com.fallgamlet.dnestrcinema.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RangesRecyclerAdapter(
        private val ranges: List<RangeItem>
) : RecyclerView.Adapter<SimpleViewHolder>() {

    override fun getItemCount(): Int = ranges.sumBy { it.itemCountProvider() }

    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val rangePoint = getRangePosition(position = viewType)
        return ranges[rangePoint.first]
                .viewHolderFactory(parent, rangePoint.second)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        val rangePoint = getRangePosition(position)
        holder.applyData(rangePoint.second)
    }

    private fun getRangePosition(position: Int): Pair<Int, Int> {
        var accumulator: Int = 0
        var localIndex: Int = -1
        val rangeIndex = ranges.indexOfFirst {
            val size = it.itemCountProvider()
            if (accumulator + size <= position) {
                accumulator += size
                false
            } else {
                localIndex = position - accumulator
                true
            }
        }

        return Pair(rangeIndex, localIndex)
    }


    class RangeItem(
            val itemCountProvider: () -> Int,
            val viewHolderFactory:  (parent: ViewGroup, position: Int) -> SimpleViewHolder
    )
}
