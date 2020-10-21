package com.example.github_connections

import com.example.github_connections.modules.util.AccountManager
import com.example.github_connections.modules.util.AccountManagerFake
import com.example.github_connections.modules.util.AppInternetChecker
import com.example.github_connections.modules.util.InternetChecker
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val testUtilsModule = Kodein.Module("TestUtilsModule") {

    bind<InternetChecker>() with singleton { AppInternetChecker(instance()) }

    bind<AccountManager>() with singleton { AccountManagerFake() }
}