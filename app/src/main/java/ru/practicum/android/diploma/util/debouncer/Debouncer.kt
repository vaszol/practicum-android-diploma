package ru.practicum.android.diploma.util.debouncer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Debouncer(private val coroutineScope: CoroutineScope, private val delayMillis: Long) {
    private var job: Job? = null

    fun debounce(action: () -> Unit) {
        job?.cancel()
        job = coroutineScope.launch { // coroutineScope нужно будет передавать при инициализации Debouncer
            delay(delayMillis)
            action()
        }
    }

    fun cancel() {
        job?.cancel()
    }
}

/*
-- Пример использования --
fun main() = runBlocking {
    val debouncer = Debouncer(viewModelScope, 500)

    repeat(10) {
        debouncer.debounce {
            println("Action executed at: ${System.currentTimeMillis()}")
        }
        delay(100)
    }

    delay(1000) // Ждем, чтобы увидеть выполнение действия
}
*/
