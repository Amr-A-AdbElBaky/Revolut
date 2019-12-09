package  com.revolut.core.extension

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.revolut.R


fun View.showSnack(@StringRes messageRes: Int, @ColorRes backgroundColor: Int = R.color.colorAlertRed, length: Int = 4000, doAction: Snackbar.() -> Unit = {}): Snackbar {
    return getSnack(message = resources.getString(messageRes), backgroundColor=backgroundColor,length = length,doAction = doAction)
}

fun View.getSuccessSnack(@StringRes messageRes: Int, length: Int = 4000, doAction: Snackbar.() -> Unit = {}) :Snackbar {
    return showSnack(messageRes,backgroundColor = R.color.colorSuccessGreen)
}
fun View.showSnackNetworkError(): Snackbar {
    return this.getSnack(context.resources.getString(R.string.screens_error_messages_internet_down))
}

fun View.showSnackUnExpectedError(): Snackbar {
    return this.getSnack(context.resources.getString(R.string.screens_error_messages_unExpected))
}


fun View.getSnack(message: String, @ColorRes backgroundColor: Int = R.color.colorAlertRed, length: Int = 4000,
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

fun Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}




fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}


