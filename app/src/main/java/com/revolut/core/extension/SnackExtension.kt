package  com.revolut.core.extension

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import com.google.android.material.snackbar.Snackbar
import com.revolut.R

fun View.getSnack(message: String, @ColorRes backgroundColor: Int = R.color.colorAlertRed, length: Int =  Snackbar.LENGTH_INDEFINITE,
                   doAction: Snackbar.() -> Unit = {}, gravity: Int = Gravity.BOTTOM): Snackbar {
    val snack = Snackbar.make(this, message.replace("\n",""), length)
    snack.doAction()
    with(snack.view) {
        val params = layoutParams as FrameLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.gravity = gravity
        layoutParams = params
        setBackgroundResource(backgroundColor)
    }

    val mTextView = snack.view.findViewById(R.id.snackbar_text) as TextView
    mTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    mTextView.maxLines=1000
    // snack.show()
    return snack
}




fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}


