import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlin.plugin.serialization)
}

kotlin {
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("androidx.datastore:datastore-preferences:1.1.1")
            //implementation(projects.shared)

            //corrutinas.
            // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
            //navegacion
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.1")
            // implementation("org.jetbrains.androidx.navigation:navigation- compose:2.9.0")
            //fecha y hora
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

            //SLF4 (salta errores de loggers, los cuales no muestran

            //SLF4J(W): No SLF4J providers were found.
            //SLF4J(W): Defaulting to no-operation (NOP) logger implementation
            implementation("ch.qos.logback:logback-classic:1.4.11")

            //iconos
            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
            //injección de dependencias
            implementation("io.insert-koin:koin-compose:4.1.1")
            implementation("io.insert-koin:koin-compose-viewmodel:4.1.1")
            implementation("io.insert-koin:koin-compose-viewmodel-navigation:4.1.1")
            //carga de imagenes
            // https://mvnrepository.com/artifact/io.coil-kt.coil3/coil
            // Imágenes
            implementation("io.coil-kt.coil3:coil-compose:3.3.0")
            // implementation("io.coil-kt.coil3:coil:3.3.0")

            implementation("io.coil-kt.coil3:coil-network-ktor3:3.3.0")
            //ktor para servicios
            implementation(libs.ktor.client.core)
            implementation("io.ktor:ktor-client-auth:3.3.0")
            implementation(libs.ktor.client.content.negotiation)
            implementation("io.ktor:ktor-server-cors:3.3.0")
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.0")
            implementation("io.ktor:ktor-client-logging:3.3.0")


            //tamanyo pantalla
            implementation("org.jetbrains.compose.material3.adaptive:adaptive:1.1.2")
            //json
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
            //diseños canonicos
            implementation("org.jetbrains.compose.material3.adaptive:adaptive:1.2.0")
            implementation("org.jetbrains.compose.material3.adaptive:adaptive-layout:1.2.0")
            implementation("org.jetbrains.compose.material3.adaptive:adaptive-navigation:1.2.0")
            //viewmodel en koin
            //implementation("io.insert-koin:koin-core-viewmodel:2.3.0")
            //implementation("io.insert-koin:koin-compose-viewmodel:2.3.0")


            //seleccion de ficheros
            implementation("io.github.vinceglb:filekit-core:0.12.0")
            implementation("io.github.vinceglb:filekit-dialogs:0.12.0")
            implementation("io.github.vinceglb:filekit-dialogs-compose:0.12.0")
            implementation("io.github.vinceglb:filekit-coil:0.12.0")

            //almacenamiento token seguro
            implementation("com.russhwolf:multiplatform-settings:1.3.0")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.client.cio)
        }
    }
}


compose.desktop {
    application {
        mainClass = "org.six.series.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.six.series"
            packageVersion = "1.0.0"
        }
    }
}
