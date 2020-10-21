package com.example.github_connections.modules.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github_connections.BaseController
import com.example.github_connections.MainActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance

inline fun <reified VM> Kodein.Builder.bindViewModel(overrides: Boolean? = null): Kodein.Builder.TypeBinder<VM>
        where VM : ViewModel = bind<VM>(tag = VM::class.java.simpleName, overrides = overrides)

inline fun <reified VM, T> T.viewModel(kodein: Kodein): Lazy<VM>
        where VM : ViewModel, T : BaseController = lazy {
    ViewModelProvider(activity as MainActivity, ViewModelFactory(kodein)).get(VM::class.java)
}

inline fun <reified VM, T> T.viewModel(): Lazy<VM>
        where VM : ViewModel, T : BaseController, T : KodeinAware = viewModel(kodein)

class ViewModelFactory(private val kodein: Kodein) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <VM> create(modelClass: Class<VM>): VM where VM : ViewModel =
        kodein.direct.instance<ViewModel>(tag = modelClass.simpleName) as VM
}