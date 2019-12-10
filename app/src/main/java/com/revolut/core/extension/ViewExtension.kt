package  com.revolut.core.extension

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText


fun ViewGroup.getInflatedView(layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}

fun EditText.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s.toString().isEmpty())
                cb("0")
            else
                cb(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}


fun EditText.onFocusChange(action: (isFocused :Boolean) -> Unit) {
    setOnFocusChangeListener { view, b ->
        action(b)
    }
}

