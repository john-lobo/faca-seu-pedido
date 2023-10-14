package com.jlndev.coreandroid.bases.adapter

abstract class BaseAdapterController<MODEL : BaseDiffItemView, VH : BaseViewHolder<MODEL>, LISTENER : BaseAdapterListener<MODEL>>(
    listener: LISTENER
) : BaseAdapter<MODEL, VH, LISTENER>(listener) {

    fun addItem(item: MODEL) {
        val newItems = items.toMutableList()
        newItems.add(item)
        submitList(newItems)
    }

    fun removeItem(item: MODEL) {
        val index = items.indexOf(item)
        if (index >= 0) {
            val newItems = items.toMutableList()
            newItems.remove(item)
            submitList(newItems)
        }
    }

    fun clear() {
        submitList(emptyList())
    }

    fun addAll(newItems: List<MODEL>) {
        val updatedItems = items.toMutableList()
        updatedItems.addAll(newItems)
        submitList(updatedItems)
    }
}

