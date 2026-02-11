package org.six.series

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.six.series.di.appModulo
import java.util.prefs.Preferences

val DesktopPlatformModule = module {
    single<Settings> {
        val preferences = Preferences.userRoot().node("mi.app")
        PreferencesSettings(preferences)
    }
}
fun main() {
    startKoin {
        printLogger()
        modules(appModulo, DesktopPlatformModule)
    }

    application {
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .components {
                    add(KtorNetworkFetcherFactory())
                }
                .build()
        }
        Window(
            onCloseRequest = ::exitApplication,
            title = "SixSeries Client",
        ) {
            App()
        }
    }
}