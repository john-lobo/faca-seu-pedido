package com.jlndev.coreandroid.bases.adapter

import androidx.recyclerview.widget.DiffUtil

class BaseDiffUtilCallback<MODEL: BaseDiffItemView>(
    private val oldList: List<MODEL>,
    private val newList: List<MODEL>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}