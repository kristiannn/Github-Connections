package com.example.github_connections.modules.friends

import com.example.github_connections.modules.util.bindViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val friendsModule = Kodein.Module("FriendsModule") {

    bindViewModel<FriendsViewModel>() with provider { FriendsViewModel(instance(), instance()) }
}