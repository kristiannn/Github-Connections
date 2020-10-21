package com.example.github_connections.modules.util

abstract class Event<T>(private val content: T) {

    private var handled = false

    fun getContent(): T? =
        if (handled) {
            null
        } else {
            handled = true
            content
        }
}