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
import org.six.series.application.usecases.content.GetEpisodesUseCase
import org.six.series.application.usecases.genre.GetGenresUseCase
import org.six.series.application.usecases.payment.GetMyPaymentsUseCase
import org.six.series.application.usecases.payment.MakePaymentUseCase
import org.six.series.application.usecases.profile.CreateProfileUseCase
import org.six.series.application.usecases.profile.GetMyProfilesUseCase
import org.six.series.application.usecases.profile.UpdateProfileUseCase
import org.six.series.application.usecases.profile.UploadAvatarUseCase
import org.six.series.application.usecases.subscription.CancelSubscriptionUseCase
import org.six.series.application.usecases.subscription.CreateSubscriptionUseCase
import org.six.series.application.usecases.subscription.GetMySubscriptionUseCase
import org.six.series.application.usecases.user.LogOutUseCase
import org.six.series.application.usecases.user.LoginUseCase
import org.six.series.application.usecases.user.RegisterUseCase
import org.six.series.infrastructure.RestContentRepository
import org.six.series.infrastructure.RestGenreRepository
import org.six.series.infrastructure.RestPaymentRepository
import org.six.series.infrastructure.RestProfileRepository
import org.six.series.infrastructure.RestSubscriptionRepository
import org.six.series.infrastructure.RestUserRepository
import org.six.series.model.content.IContentRepository
import org.six.series.model.genre.IGenreRepository
import org.six.series.model.payment.IPaymentRepository
import org.six.series.model.profile.IProfileRepository
import org.six.series.model.subscripion.ISubscriptionRepository
import org.six.series.ui.appsettings.AppSettings
import org.six.series.ui.components.viewmodels.DetailViewModel
import org.six.series.ui.components.viewmodels.MainPageViewModel
import org.six.series.ui.components.viewmodels.ProfileViewModel
import org.six.series.ui.components.viewmodels.SubscriptionViewModel

private const val url: String = "http://localhost:8000"

val appModulo = module {

    // ── Token Storage ──
    single { TokenStorage(get()) }

    // ── HTTP Client (with auto-refresh) ──
    single {
        createHttpClient(get(), "$url/users/refresh/")
    }

    // ── App Settings (theme color DataStore) ──
    single {
        AppSettings(databasePath = "app_settings.preferences_pb")
    }

    // ── Repositories ──
    single<IUserRepository> {
        RestUserRepository(url = "$url/users", cliente = get(), tokenStorage = get())
    }
    single<IContentRepository> {
        RestContentRepository(url = "$url/contents", cliente = get(), tokenStorage = get())
    }
    single<IGenreRepository> {
        RestGenreRepository(url = "$url/genres", cliente = get(), tokenStorage = get())
    }
    single<ISubscriptionRepository> {
        RestSubscriptionRepository(url = "$url/subscription", cliente = get(), tokenStorage = get())
    }
    single<IPaymentRepository> {
        RestPaymentRepository(url = "$url/payments", cliente = get(), tokenStorage = get())
    }
    single<IProfileRepository> {
        RestProfileRepository(url = "$url/users/profiles", cliente = get(), tokenStorage = get())
    }

    // ── Use Cases ──
    single { LogOutUseCase(userRepository = get()) }
    factory { RegisterUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { GetContentUseCase(get()) }
    factory { GetGenresUseCase(get()) }
    factory { GetMySubscriptionUseCase(get()) }
    factory { CreateSubscriptionUseCase(get()) }
    factory { CancelSubscriptionUseCase(get()) }
    factory { GetMyPaymentsUseCase(get()) }
    factory { MakePaymentUseCase(get()) }
    factory { GetMyProfilesUseCase(get()) }
    factory { UpdateProfileUseCase(get()) }
    factory { UploadAvatarUseCase(get()) }
    factory { CreateProfileUseCase(get()) }
    factory { GetEpisodesUseCase(get()) }
    // ── ViewModels ──
    viewModel { AppViewModel(get(), get(), get()) }
    viewModel { LoginFormViewModel(loginUseCase = get()) }
    viewModel { RegisterFormViewModel(get()) }
    viewModel { MainPageViewModel(get(), get(), get(), get()) }
    viewModel {
        ProfileViewModel(
            getMyProfilesUseCase = get(),
            createProfileUseCase = get(),
            updateProfileUseCase = get(),
            appViewModel = get()
        )
    }
    viewModel {
        SubscriptionViewModel(
            getMySubscriptionUseCase = get(),
            createSubscriptionUseCase = get(),
            cancelSubscriptionUseCase = get(),
            getMyPaymentsUseCase = get(),
            makePaymentUseCase = get()
        )
    }
    viewModel { DetailViewModel(get()) }
}
