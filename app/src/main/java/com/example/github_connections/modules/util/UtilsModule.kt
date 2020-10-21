package com.example.github_connections.modules.util

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val utilsModule = Kodein.Module("UtilsModule") {

    bind<InternetChecker>() with singleton { AppInternetChecker(instance()) }

    bind<AccountManager>() with singleton { UserAccountManager(instance()) }
}