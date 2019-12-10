package com.revolut.base.presentation.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.revolut.core.extension.toInt

/**
 *base class for recycler view adapter that can has header & footer to recycler view
 *
 * @property showHeader Boolean
 * @property showFooter Boolean
 * @property count Int
 */
abstract class BaseRecyclerAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var showHeader = false
    protected var showFooter = false
    var data: MutableList<T> = mutableListOf()
    private fun getCount(): Int = getItems().size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val v = headerItemView(parent)
                getHeaderItemHolder(v)
            }
            TYPE_FOOTER -> {
                val v = footerItemView(parent)
                getFooterItemHolder(v)
            }
            else -> {
                val v = mainItemView(parent)
                mainItemViewHolder(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var newPos = position

        if (showHeader) newPos = position - 1

        onBindMainViewHolder(holder, newPos)
    }

    override fun getItemCount(): Int {
        return getCount() + showFooter.toInt() + showHeader.toInt()   //if header is shown then +1 if footer another +1
    }

    override fun getItemViewType(position: Int): Int {
        if (isShowHeader(position))
            return TYPE_HEADER
        else if (isShowFooter(position))
            return TYPE_FOOTER

        return position
    }


    open fun headerItemView(parent: ViewGroup): View {
        return View(parent.context)
    }

    open fun getHeaderItemHolder(view: View): RecyclerView.ViewHolder {
        return EmptyViewHolder(view)
    }

    open fun footerItemView(parent: ViewGroup): View {
        return View(parent.context)
    }

    open fun getFooterItemHolder(view: View): RecyclerView.ViewHolder {
        return EmptyViewHolder(view)
    }

    protected fun isShowHeader(position: Int): Boolean {
        return position == 0 && showHeader
    }

    protected fun isShowFooter(position: Int): Boolean {
        return position == itemCount - 1 && showFooter
    }

    fun showFooter(showFooter: Boolean) {
        this.showFooter = showFooter
        notifyDataSetChanged()
    }

    fun showHeader(showHeader: Boolean) {
        this.showHeader = showHeader
        notifyDataSetChanged()
    }

    fun getItems(): MutableList<T> {
        return data
    }


    fun replaceItems(items: List<T>) {
        val lastIdex = data.size-1
       data =  data.filterIndexed { index, t ->
            index == 0
        } as MutableList<T>
        notifyItemRangeChanged(1, lastIdex)
        data.addAll(items)
        notifyDataSetChanged()
      //  notifyItemRangeInserted(1, items.size-1)
    }

    fun swapToTheTop(position: Int) {
        if (position > 0) {
            data.add(0, data.removeAt(position))
            notifyDataSetChanged()
        }
    }

    fun setItemsFirstTime(items: List<T>) {
        getItems().addAll(items)
        notifyItemRangeInserted(0, items.size)
    }

    fun isEmpty(): Boolean = getItems().isEmpty()


    protected abstract fun mainItemView(parent: ViewGroup): View

    protected abstract fun mainItemViewHolder(view: View): RecyclerView.ViewHolder

    protected abstract fun onBindMainViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    private inner class EmptyViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    fun getFooterItemPosition() = TYPE_FOOTER
    fun getHeaderItemPosition() = TYPE_HEADER

    companion object {
        const val TYPE_HEADER = -1
        const val TYPE_FOOTER = -2
    }
}
