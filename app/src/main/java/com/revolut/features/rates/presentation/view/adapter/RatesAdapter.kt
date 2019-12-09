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
                tvRateTitle.text = item.name
                if (position != 0) {
                    edtRate.setText(getCurrencyValue(item.value).toString())
                } else {
                    if (edtRate.getTextString().isEmpty() && item.value == 0.0)
                        edtRate.setText("1.0")
                    else
                        edtRate.setText(item.value.toString())


            /*        if (focusedPosition!= -1)
                        edtRate.requestFocus()*/
                }

                edtRate.onChange {
                    if (it!= item.value.toString()){
                    currantRateValue = if (it.isEmpty())
                        0.0
                    else
                        it.toDouble()
               //     getItems()[position].value = currantRateValue

                    item.value = currantRateValue
                    onCurrencyClick?.invoke(item)
                    }
                }
                edtRate.onFocusChange{
                    if ( position != 0 ) {
                        focusedPosition = position
                        getItems()[position].value = getCurrencyValue(item.value)
                     //   swapToTheTop(position)
                        onCurrencyClick?.invoke(item)
                    //    edtRate.requestFocus()
                    }

                }
            }
        }


        private fun getCurrencyValue(baseValue: Double): Double {
            return (baseValue * currantRateValue)

        }
    }

}



