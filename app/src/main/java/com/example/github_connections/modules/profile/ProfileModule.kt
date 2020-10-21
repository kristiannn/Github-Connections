package com.example.github_connections.modules.profile

import com.example.github_connections.modules.util.bindViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val profileModule = Kodein.Module("ProfileModule") {

    bindViewModel<ProfileViewModel>() with provider { ProfileViewModel(instance(), instance()) }
}