package  com.revolut.core.extension


fun String.isValidDouble(): Boolean{ return this.toDoubleOrNull() != null}

fun Boolean.toInt() = if (this) 1 else 0
