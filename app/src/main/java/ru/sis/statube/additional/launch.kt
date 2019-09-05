package ru.sis.statube.additional

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun launch(block: suspend CoroutineScope.() -> Unit,
           onError: (e: Exception) -> Unit) {
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