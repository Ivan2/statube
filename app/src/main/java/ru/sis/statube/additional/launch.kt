package ru.sis.statube.additional

import kotlinx.coroutines.*

fun launch(block: suspend CoroutineScope.() -> Unit, onError: (e: Exception) -> Unit) {
    GlobalScope.launch(Dispatchers.Main) {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
            onError(e)
        }
    }
}

fun launch(block: suspend CoroutineScope.() -> Unit) = launch(block) {}

fun <T> async(block: suspend CoroutineScope.() -> T) = GlobalScope.async(block = block)