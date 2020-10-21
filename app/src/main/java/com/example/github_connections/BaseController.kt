package com.example.github_connections

import android.os.Bundle
import com.bluelinelabs.conductor.archlifecycle.LifecycleController

abstract class BaseController(bundle: Bundle? = null) : LifecycleController(bundle) {

    init {
        retainViewMode = RetainViewMode.RETAIN_DETACH
    }
}