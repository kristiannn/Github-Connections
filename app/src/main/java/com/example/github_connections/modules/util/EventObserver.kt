package com.example.github_connections.modules.util

import androidx.lifecycle.Observer

class EventObserver<T : Event<*>>(private val handler: (T, Any) -> Unit) : Observer<T> {

    override fun onChanged(t: T) {
        val content = t.getContent()
        if (content != null) {
            handler(t, content)
        }
    }
}