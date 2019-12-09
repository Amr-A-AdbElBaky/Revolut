package  com.revolut.core.extension

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.revolut.base.presentation.view.adapter.BaseRecyclerAdapter
import com.revolut.R
import com.revolut.base.domain.Identifiable
import com.revolut.base.presentation.view.adapter.DiffCallback
import com.revolut.core.presentation.view.custom.StateView


enum class ViewPosition {
    TOP,
    BOTTOM
}

fun ViewGroup.loadLayout(layout: Int) {
    LayoutInflater.from(context).inflate(layout, this, true)
}

fun View.location(): IntArray {
    val array = IntArray(2)
    this.getLocationOnScreen(array)
    return array
}

fun ViewGroup.inflate(layout: Int) {
    LayoutInflater.from(context).inflate(layout, this)

}

fun ViewGroup.getInflatedView(layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}

fun LayoutInflater.getInflatedView(layout: Int, container: ViewGroup?): View {
    return inflate(layout, container, false)
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}


fun EditText.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (this@onChange.getTextString()!= s.toString())
                cb(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun View.disable(withAlpha: Boolean = true) {
    this.isEnabled = false
    if (withAlpha) this.alpha = 0.4f
}

fun View.enable(withAlpha: Boolean = true) {
    this.isEnabled = true
    if (withAlpha) this.alpha = 1.0f
}



fun View.sameClickListener(vararg views: View, action: () -> Unit) {
    val clickListener = View.OnClickListener { action.invoke() }
    this.setOnClickListener(clickListener)
    for (v in views)
        v.setOnClickListener(clickListener)
}


fun TextInputEditText.isNotEmpty(): Boolean {
    return this.text.toString().isNotEmpty()
}

fun TextInputEditText.getTextString() = this.text.toString()

fun EditText.isNotEmpty(): Boolean {
    return this.text.toString().isNotEmpty()
}

fun EditText.getTextString() = this.text.toString()

fun EditText.onTextChanged(action: () -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            action()
        }
    })
}

fun EditText.onKeyboardActionClicked(action: () -> Unit) {
    setOnEditorActionListener { textView, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            action()
        }
        /*
        * Return:-
        * True: if you consumed the action (it'll keep the keyboard open)
        * False: if you don't consume the action (it'll close the keyboard after action clicked)
        * */
        false
    }
}

fun EditText.onFocusChange(action: (isFocused :Boolean) -> Unit) {
    setOnFocusChangeListener { view, b ->
        action(b)
    }
}


fun TextView.show(@StringRes msg: Int) {
    this.setText(msg)
    this.visible()
}

fun TextView.hide() {
    this.text = ""
    this.invisible()
}

fun View.setHeight(heightInPx:Int){
    val newParams = layoutParams
    newParams.height=heightInPx
    layoutParams=newParams
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun NestedScrollView.ifScrollableScrollBy(value: Int) {
    viewTreeObserver.addOnGlobalLayoutListener {
        val viewHeight = measuredHeight
        val contentHeight = getChildAt(0).height
        if(viewHeight - contentHeight < 0) {
            // scrollable
            smoothScrollBy(0, value)
        }
    }
}


fun <T : Identifiable> BaseRecyclerAdapter<T>.swap(items: List<T>) {
    val diffCallback = DiffCallback(data, items)
    val diffResult = DiffUtil.calculateDiff(diffCallback)
    data.clear()
    data.addAll(items)
    diffResult.dispatchUpdatesTo(this)
}

fun ViewGroup.showSnackBar(msg: String, @ColorRes color: Int = R.color.colorAlertRed, length: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, msg, length).show()
}
//to show title in case of empty
fun StateView.setTransparentEmpty(msg:String, @LayoutRes layout:Int?=null){
    layout?.let {setEmpty(msg,it)} ?: setEmpty(msg)
    setBackgroundColor(Color.TRANSPARENT)
}

