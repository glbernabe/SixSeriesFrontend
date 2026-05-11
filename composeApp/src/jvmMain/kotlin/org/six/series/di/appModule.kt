package org.six.series.di

import org.six.series.model.user.IUserRepository
import org.six.series.ui.components.register.RegisterFormViewModel
import org.six.series.ui.components.login.LoginFormViewModel
import org.six.series.ui.appsettings.AppViewModel
import org.six.series.infrastructure.TokenStorage
import org.six.series.infrastructure.ktor.createHttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.six.series.application.usecases.content.GetContentUseCase
import org.six.series.application.usecases.user.LogOutUseCase
import org.six.series.application.usecases.user.LoginUseCase
import org.six.series.application.usecases.user.RegisterUseCase
import org.six.series.infrastructure.RestContentRepository
import org.six.series.infrastructure.RestUserRepository
import org.six.series.model.content.IContentRepository
import org.six.series.ui.appsettings.AppSettings
import org.six.series.ui.components.viewmodels.MainPageViewModel

private const val url: String = "http://localhost:8000"
val appModulo = module {

    // Token Storage
    single { TokenStorage(get()) }

    single {
        createHttpClient( get(),
            "$url/api/public/refresh"
        )
    }



    // Repository
    single<IUserRepository> {
        RestUserRepository(
            url = "$url/users",
            cliente = get(),
            tokenStorage = get()
        )
    }

    single<IContentRepository> {
        RestContentRepository(
            url = "$url/contents",
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
    viewModel { MainPageViewModel(get() ) }
    factory { RegisterUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { GetContentUseCase(get()) }

}