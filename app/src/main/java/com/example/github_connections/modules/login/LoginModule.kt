package com.example.github_connections.modules.login

import com.example.github_connections.modules.util.bindViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val loginModule = Kodein.Module("LoginModule") {

    bindViewModel<LoginViewModel>() with provider { LoginViewModel(instance(), instance()) }
}