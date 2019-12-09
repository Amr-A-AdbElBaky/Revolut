package  com.revolut.core.extension


import com.google.gson.Gson
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

inline fun <reified T> String?.fromJSON(): T? {
    return this?.run { Gson().fromJson(this, T::class.java) }
}

inline fun <reified T> T?.toJson(): String? {
    return this?.run { Gson().toJson(this, T::class.java) }
}


fun Boolean.toInt() = if (this) 1 else 0

fun Int.toBoolean() = this == 1

fun Int.percentage(total: Int): Int {
    val num = (this.toFloat() * 100f / total.toFloat()).roundToInt()
    return if (num <= 100) num else 100
}

fun Double.format(): String {
    val df = DecimalFormat("###.#")
    return df.format(this)
}

fun Float.format(): String {
    val df = DecimalFormat("###.#")
    return df.format(this)
}

fun String.locale(languageCode: String): String {
    return try {
        val locale = Locale(languageCode)
        val nf = NumberFormat.getInstance(locale)
        nf.format(this.toDouble())
    } catch (e: Exception) {
        this
    }
}


inline fun <reified T> String.toObject(): T {
    return Gson().fromJson(this, T::class.java)
}
