package com.jlndev.coreandroid.bases.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<MODEL : BaseDiffItemView, VH : BaseViewHolder<MODEL>, LISTENER : BaseAdapterListener<MODEL>>(
    open var listener: LISTENER
) : RecyclerView.Adapter<VH>() {

    protected var items: List<MODEL> = emptyList()

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            listener.onAdapterItemClicked(position, items[position], holder.itemView)
        }
    }

    open fun submitList(newItems: List<MODEL>) {
        val diffResult = DiffUtil.calculateDiff(BaseDiffUtilCallback(items, newItems))
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }
}
