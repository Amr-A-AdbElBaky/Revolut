package com.revolut.base.presentation.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.revolut.base.domain.Identifiable

class DiffCallback(
    private val oldList: List<Identifiable>,
    private val newList: List<Identifiable>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].identifier == newList[newItemPosition].identifier
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }

}

