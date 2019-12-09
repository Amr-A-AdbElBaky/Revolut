package com.revolut.core.presentation.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.revolut.R
import kotlinx.android.synthetic.main.layout_state_connection_failure_view.view.*
import kotlinx.android.synthetic.main.layout_state_empty_view.view.*
import kotlinx.android.synthetic.main.layout_state_unexpected_failure.view.*


open class StateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    fun setLoading() {
        showState {
            return@showState inflate(context, R.layout.layout_state_loading_view, null)
        }
        setOnTouchListener { _, _ ->
            true
        }
    }

    fun setNetworkError(message: String = context.getString(R.string.screens_error_messages_internet_down),
                        retryMessage: String = context.getString(R.string.no_internet_screens_retry_btn_title),
                        retryAction: (() -> Unit)? = null) {
        showState {
            val stateView = inflate(context, R.layout.layout_state_connection_failure_view, null)
            stateView.tvNoConnection?.text = message
            stateView.btnRetryConnection?.text = retryMessage
            stateView.btnRetryConnection?.setOnClickListener { retryAction?.invoke() }
            return@showState stateView
        }
    }

    fun setUnexpectedError(message: String = context.getString(R.string.screens_error_messages_unExpected),
                           retryMessage: String = context.getString(R.string.no_internet_screens_retry_btn_title),
                           retryAction: (() -> Unit)? = null) {
        showState {
            val stateView = inflate(context, R.layout.layout_state_unexpected_failure, null)
            stateView.tvUnexpected?.text = message
            stateView.btnRetryUnexpected?.text = retryMessage
            stateView.btnRetryUnexpected?.setOnClickListener { retryAction?.invoke() }
            return@showState stateView
        }
    }

    fun setEmpty(message: String,@LayoutRes layout:Int=R.layout.layout_state_empty_view) {
        showState {
            val stateView = inflate(context,layout, null)
            stateView.tvEmptyItems?.text = message
            return@showState stateView
        }
    }

    fun setContent() {
        if (visibility != View.GONE) {
            removeAllViews()
            visibility = View.GONE
        }
        setOnTouchListener(null)
    }

    protected fun showState(inflater: (() -> View)) {
        removeAllViews()
        val frameParams = LayoutParams(LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT).apply { gravity = Gravity.CENTER }
        addView(inflater.invoke(), frameParams)
        visibility = View.VISIBLE
    }

}