package ru.practicum.android.diploma.util.extentions

import android.content.Context
import kotlin.math.roundToInt

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).roundToInt()
}

/*
-- Пример использования --
fun main() {
    val context = getApplicationContext()
    val dp = 24
    val px = context.dpToPx(dp)
    println("Converted dp to px: $px")
}
 */
