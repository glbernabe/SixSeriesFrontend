package org.six.series.di

import org.six.series.model.IUserRepository
import org.six.series.ui.components.register.RegisterFormViewModel
import org.six.series.ui.components.login.LoginFormViewModel
import org.six.series.ui.appsettings.AppViewModel
import org.six.series.infrastructure.TokenStorage
import org.six.series.infrastructure.ktor.createHttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.six.series.application.usecases.LogOutUseCase
import org.six.series.application.usecases.LoginUseCase
import org.six.series.application.usecases.RegisterUseCase
import org.six.series.infrastructure.RestUserRepository
import org.six.series.ui.appsettings.AppSettings


val appModulo = module {

    // Token Storage
    single { TokenStorage(get()) }

    single {
        createHttpClient( get(),
            "http://localhost:8080/api/public/refresh"
        )
    }

    // Repository
    single<IUserRepository> {
        RestUserRepository(
            url = "http://localhost:8000/users",
            cliente = get(),
            tokenStorage = get()
        )
    }

    single {
        AppSettings(databasePath = "app_settings.preferences_pb")
    }

    single {
        LogOutUseCase(userRepository = get())
    }

    viewModel { AppViewModel(get(), get(), get()) }
    viewModel { LoginFormViewModel(get()) }

    viewModel { RegisterFormViewModel(get()) }
    factory { RegisterUseCase(get()) }
    factory { LoginUseCase(get()) }

}