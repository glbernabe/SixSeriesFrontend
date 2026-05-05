package org.six.series.di

import org.six.series.model.IUserRepository
import ies.sequeros.dam.pmdm.gestionperifl.ui.appsettings.AppSettings
import org.six.series.ui.components.register.RegisterFormViewModel
import org.six.series.ui.components.login.LoginFormViewModel
import org.six.series.ui.appsettings.AppViewModel
import org.six.series.infrastructure.TokenStorage
import org.six.series.infrastructure.ktor.createHttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.six.series.application.usecases.LoginUseCase
import org.six.series.application.usecases.RegisterUseCase
import org.six.series.infrastructure.RestUserRepository


val appModulo = module {

    /**
     * infraestructura
     */
    //almacenamiento del token
    single { TokenStorage(get()) }

    single {
        createHttpClient( get(),
            "http://localhost:8080/api/public/refresh"
        )
    }






    //repositorios
    single<IUserRepository> {
        RestUserRepository(
            url = "http://localhost:8000/users",
            cliente = get(),
            tokenStorage = get()
        )
    }

    /**
    capa de aplicación
    el sesion manager,
    el origen de los datos, se encarga de transforar el tokenstorage para trabajar con user
    casos de uso
     **/

    /**
    capa de presentación
     **/
    single { AppSettings() }
    viewModel { AppViewModel(get(), get(), get()) }
    viewModel { LoginFormViewModel(get()) }

    viewModel { RegisterFormViewModel(get()) }
    factory { RegisterUseCase(get()) }
    factory { LoginUseCase(get()) }

}