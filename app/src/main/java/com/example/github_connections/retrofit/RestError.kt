package com.example.github_connections.retrofit

sealed class RestError(message: String) : Throwable(message) {

    class HttpError(val body: String, val code: Int) : RestError("Http error $code: $body")

    object NetworkError : RestError("Unable to gather results due to connectivity issues.")

    object EmptyBody : RestError("Missing user!")
}