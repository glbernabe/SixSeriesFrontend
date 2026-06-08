package org.six.series.di

import org.six.series.model.user.IUserRepository
import org.six.series.ui.components.register.RegisterFormViewModel
import org.six.series.ui.components.login.LoginFormViewModel
import org.six.series.ui.appsettings.AppViewModel
import org.six.series.infrastructure.TokenStorage
import org.six.series.infrastructure.ktor.createHttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.six.series.application.usecases.content.AddContentUseCase
import org.six.series.application.usecases.content.AddEpisodeUseCase
import org.six.series.application.usecases.content.DeleteContentUseCase
import org.six.series.application.usecases.content.DeleteEpisodeUseCase
import org.six.series.application.usecases.content.GetContentUseCase
import org.six.series.application.usecases.content.GetEpisodesUseCase
import org.six.series.application.usecases.content.ModifyEpisodeUseCase
import org.six.series.application.usecases.content.UpdateContentUseCase
import org.six.series.application.usecases.favorite.AddFavoriteUseCase
import org.six.series.application.usecases.favorite.GetMyFavoritesUseCase
import org.six.series.application.usecases.favorite.RemoveFavoriteUseCase
import org.six.series.application.usecases.genre.AddContentToGenreUseCase
import org.six.series.application.usecases.genre.AddGenreUseCase
import org.six.series.application.usecases.genre.DeleteGenreUseCase
import org.six.series.application.usecases.genre.GetContentByGenreUseCase
import org.six.series.application.usecases.genre.GetGenresUseCase
import org.six.series.application.usecases.genre.UpdateGenreUseCase
import org.six.series.application.usecases.history.GetHistoryUseCase
import org.six.series.application.usecases.history.SaveHistoryUseCase
import org.six.series.application.usecases.payment.GetMyPaymentsUseCase
import org.six.series.application.usecases.payment.MakePaymentUseCase
import org.six.series.application.usecases.profile.CreateProfileUseCase
import org.six.series.application.usecases.profile.GetMyProfilesUseCase
import org.six.series.application.usecases.profile.UpdateProfileUseCase
import org.six.series.application.usecases.profile.UploadAvatarUseCase
import org.six.series.application.usecases.subscription.CancelSubscriptionUseCase
import org.six.series.application.usecases.subscription.CreateSubscriptionUseCase
import org.six.series.application.usecases.subscription.GetMySubscriptionUseCase
import org.six.series.application.usecases.user.DeleteUserByIdUseCase
import org.six.series.application.usecases.user.GetAllUsersUseCase
import org.six.series.application.usecases.user.LogOutUseCase
import org.six.series.application.usecases.user.LoginUseCase
import org.six.series.application.usecases.user.RegisterUseCase
import org.six.series.application.usecases.user.UpdateUserAccountUseCase
import org.six.series.application.usecases.user.UpdateUserStatusUseCase
import org.six.series.infrastructure.RestContentRepository
import org.six.series.infrastructure.RestFavoriteRepository
import org.six.series.infrastructure.RestGenreRepository
import org.six.series.infrastructure.RestHistoryRepository
import org.six.series.infrastructure.RestPaymentRepository
import org.six.series.infrastructure.RestProfileRepository
import org.six.series.infrastructure.RestSubscriptionRepository
import org.six.series.infrastructure.RestUserRepository
import org.six.series.model.content.IContentRepository
import org.six.series.model.favorite.IFavoriteRepository
import org.six.series.model.genre.IGenreRepository
import org.six.series.model.history.IHistoryRepository
import org.six.series.model.payment.IPaymentRepository
import org.six.series.model.profile.IProfileRepository
import org.six.series.model.subscription.ISubscriptionRepository
import org.six.series.ui.appsettings.AppSettings
import org.six.series.ui.components.basic.genre.GenreDetailViewModel
import org.six.series.ui.components.screens.admin.viewmodel.AdminPanelViewModel
import org.six.series.ui.components.viewmodels.DetailViewModel
import org.six.series.ui.components.viewmodels.MainPageViewModel
import org.six.series.ui.components.viewmodels.ProfileViewModel
import org.six.series.ui.components.viewmodels.SubscriptionViewModel

private const val url: String = "http://localhost:8000"

val appModulo = module {

    // ── Token Storage ──
    single { TokenStorage(get()) }

    // ── HTTP Client ──
    single {
        createHttpClient(get(), "$url/users/refresh/")
    }

    // ── App Settings ──
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
    single<IFavoriteRepository> {
        RestFavoriteRepository(url = "$url/favorite", cliente = get(), tokenStorage = get())
    }
    single<IHistoryRepository> {
        RestHistoryRepository(url = "$url/history", cliente = get())
    }

    // ── Use Cases ──
    single { LogOutUseCase(userRepository = get()) }
    factory { RegisterUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { GetContentUseCase(get()) }
    factory { GetGenresUseCase(get()) }
    factory { GetContentByGenreUseCase(get()) }
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
    factory { AddFavoriteUseCase(get()) }
    factory { RemoveFavoriteUseCase(get()) }
    factory { GetMyFavoritesUseCase(get()) }
    factory { SaveHistoryUseCase(get()) }
    factory { GetHistoryUseCase(get()) }

    // ── Casos de Uso del Panel de Administración (Nuevos) ──
    factory { GetAllUsersUseCase(get()) }
    factory { UpdateUserStatusUseCase(get()) }
    factory { UpdateUserAccountUseCase(get()) }
    factory { DeleteUserByIdUseCase(get()) }
    factory { AddContentUseCase(get()) }
    factory { UpdateContentUseCase(get()) }
    factory { DeleteContentUseCase(get()) }
    factory { AddEpisodeUseCase(get()) }
    factory { ModifyEpisodeUseCase(get()) }
    factory { DeleteEpisodeUseCase(get()) }
    factory { AddGenreUseCase(get()) }
    factory { UpdateGenreUseCase(get()) }
    factory { DeleteGenreUseCase(get()) }
    factory { AddContentToGenreUseCase(get()) }

    // ── ViewModels ──
    viewModel { AppViewModel(get(), get(), get()) }
    viewModel { LoginFormViewModel(loginUseCase = get()) }
    viewModel { RegisterFormViewModel(get()) }
    viewModel {
        MainPageViewModel(
            context = get(),
            getContentUseCase = get(),
            getGenresUseCase = get(),
            getContentByGenreUseCase = get(),
            imageLoader = get(),
            addFavoriteUseCase = get(),
            removeFavoriteUseCase = get(),
            getMyFavoritesUseCase = get(),
            saveHistoryUseCase = get(),
            getHistoryUseCase = get(),
            settings = get()
        )
    }
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
    viewModel { (genreName: String) ->
        GenreDetailViewModel(
            genreName = genreName,
            getContentByGenreUseCase = get()
        )
    }
    viewModel { DetailViewModel(get()) }
    viewModel {
        AdminPanelViewModel(
            getAllUsersUseCase = get(),
            getContentUseCase = get(),
            getGenresUseCase = get(),
        )
    }
}