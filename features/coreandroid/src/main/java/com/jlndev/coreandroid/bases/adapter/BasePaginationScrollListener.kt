package com.jlndev.coreandroid.bases.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jlndev.coreandroid.ext.isInternetAvailable

abstract class BasePaginationScrollListener(private val layoutManager: LinearLayoutManager, private val context: Context) :
    RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if(context.isInternetAvailable()) {
            val visibleItemCount: Int = layoutManager.childCount
            val totalItemCount: Int = layoutManager.itemCount
            val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
            if (!isLoading()) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount - 3
                    && firstVisibleItemPosition >= 0
                ) {
                    loadMoreItems()
                }
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract fun isLoading(): Boolean
}
