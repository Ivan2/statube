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
        } catch (ex: Exception) {
            ex.printStackTrace()
            /*when (ex) {
                is HttpException -> {
                    if (ex.code() >= 500)
                        onError(InternalServerException())
                    else
                        onError(ex)
                }
                is CustomException -> onError(ex)
                is UnknownHostException, is NetworkErrorException, is SocketTimeoutException, is ConnectException -> {
                    onError(NetworkException())
                }
                else -> onError(UnknownException())
            }*/
        }
    }
}