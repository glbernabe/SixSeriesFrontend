package org.six.series

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import org.jetbrains.compose.resources.painterResource
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.six.series.di.appModulo
import sixseries.composeapp.generated.resources.Res
import sixseries.composeapp.generated.resources.logo_sixSeries
import java.awt.Dimension
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
                .components { add(KtorNetworkFetcherFactory()) }
                .build()
        }

        val icon = painterResource(Res.drawable.logo_sixSeries)
        val windowState = rememberWindowState(
            placement = WindowPlacement.Maximized,
            size = DpSize(1280.dp, 800.dp)
        )

        Window(
            onCloseRequest = ::exitApplication,
            icon = icon,
            title = "Six Series",
            state = windowState
        ) {
            // We set a minimum value so the window can be redeable
            window.minimumSize = Dimension(1100, 700)
            App()
        }
    }
}