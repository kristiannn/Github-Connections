package com.example.github_connections.modules

import androidx.lifecycle.ViewModel
import com.example.github_connections.modules.friends.FriendsViewModel
import com.example.github_connections.modules.login.LoginViewModel
import com.example.github_connections.modules.profile.ProfileViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val testViewModels = Kodein.Module("TestViewModels") {

    bind<ViewModel>(tag = "LoginViewModel") with singleton {
        LoginViewModel(
            instance(),
            instance()
        )
    }

    bind<ViewModel>(tag = "ProfileViewModel") with singleton {
        ProfileViewModel(
            instance(),
            instance()
        )
    }

    bind<ViewModel>(tag = "FriendsViewModel") with singleton {
        FriendsViewModel(
            instance(),
            instance()
        )
    }
}