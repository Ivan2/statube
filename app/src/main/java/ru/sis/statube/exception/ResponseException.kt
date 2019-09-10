package ru.sis.statube.exception

class ResponseException(
    val errorCode: String?,
    val errorMessage: String?
) : Exception()