package com.revolut.features.rates.presentation.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.revolut.base.presentation.view.adapter.BaseRecyclerAdapter
import com.revolut.R
import com.revolut.core.extension.*
import com.revolut.core.misc.OneParamFunction
import com.revolut.features.rates.domain.entity.Rate
import kotlinx.android.synthetic.main.item_rate.view.*

class RatesAdapter : BaseRecyclerAdapter<Rate>() {

    var onCurrencyClick: OneParamFunction<Rate>? = null
    private var focusedPosition = -1
    private var currantRateValue = 1.0

    override fun mainItemView(parent: ViewGroup): View {
        return parent.getInflatedView(R.layout.item_rate)
    }

    override fun mainItemViewHolder(view: View): RecyclerView.ViewHolder {
        return RatesVH(view)
    }


    override fun onBindMainViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RatesVH)
            holder.bind(data[position], position)
    }

    private inner class RatesVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Rate, position: Int) =
            with(itemView) {
                bindLayout(item, position)
            }


        private fun View.bindLayout(item: Rate, position: Int) {
            with(item) {
                tvRateTitle.text = name
                tvRateSubTitle.text = name // place holder because api doesn't return subtitles && images

                if (position != 0) {
                    edtRate.setText(getCurrencyValue(value).toString())
                }else {
                    edtRate.setText(currantRateValue.toString())
                    if (focusedPosition != -1)
                        edtRate.requestFocus()
                }

                edtRate.onChange {
                    if (edtRate.hasFocus() && it.isValidDouble()) {
                        currantRateValue = if (it.isEmpty())
                            0.0
                        else
                            it.toDouble()

                        getItems()[position].value = currantRateValue
                        notifyItemRangeChanged(1, itemCount - 1)
                    }
                }

                edtRate.onFocusChange {
                    if (it && position != 0) {
                        focusedPosition = position
                        currantRateValue = value
                        onCurrencyClick?.invoke(item)
                    }
                }
            }
        }

        private fun getCurrencyValue(baseValue: Double): Double {
            return (baseValue * currantRateValue)

        }
    }

}



