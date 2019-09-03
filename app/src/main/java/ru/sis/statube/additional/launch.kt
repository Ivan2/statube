package ru.sis.statube.additional

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun resolvedLaunch(block: suspend CoroutineScope.() -> Unit,
                   onError: (ex: Exception) -> Unit) {
    GlobalScope.launch(Dispatchers.Main) {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}