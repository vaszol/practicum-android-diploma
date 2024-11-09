package ru.practicum.android.diploma.util.debouncer

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Debouncer(private val delayMillis: Long) {
    private var job: Job? = null

    fun debounce(action: () -> Unit) {
        job?.cancel()
        job = GlobalScope.launch {
            delay(delayMillis)
            action()
        }
    }
}

/*
-- Пример использования --
fun main() = runBlocking {
    val debouncer = Debouncer(500)

    repeat(10) {
        debouncer.debounce {
            println("Action executed at: ${System.currentTimeMillis()}")
        }
        delay(100)
    }

    delay(1000) // Ждем, чтобы увидеть выполнение действия
}
*/
