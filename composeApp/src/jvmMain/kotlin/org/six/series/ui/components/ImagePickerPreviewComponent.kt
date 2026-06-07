package org.six.series.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.readBytes

@Composable
fun ImagePickerPreviewComponent(
    imageUrl: String? = null,
    selectedFile: PlatformFile?,
    onFileSelected: (PlatformFile?) -> Unit,
    onConfirm: () -> Unit,
) {
    //bytes de la imagen
    var imageBytes by remember(selectedFile) { mutableStateOf<ByteArray?>(null) }

    //para abrir el cuadro de dialogo
    val launcher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        title = "Selecciona una imagen"
    ) { file ->
        onFileSelected(file)
    }
    LaunchedEffect(selectedFile) {
        imageBytes = selectedFile?.readBytes()
    }
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalPlatformContext.current // En KMP/Compose Multiplatform
        val modelToDisplay: Any? = when {
            imageBytes != null -> imageBytes // Nueva imagen (bytes)
            !imageUrl.isNullOrBlank() -> imageUrl // Imagen de red
            else -> null
        }

        val imageRequest = ImageRequest.Builder(context)
            .data(modelToDisplay)
            .memoryCachePolicy(CachePolicy.DISABLED) // Desactiva caché en RAM
            .diskCachePolicy(CachePolicy.DISABLED)   // Desactiva caché en Disco
            .networkCachePolicy(CachePolicy.READ_ONLY) // Fuerza a no escribir en caché de red
            .build()
        Card(
            modifier = Modifier.size(200.dp).padding(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            if (modelToDisplay != null) {
                AsyncImage(
                    model = imageRequest,
                    contentDescription = "Vista previa",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Sin imagen", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { launcher.launch() }) {
                Text(if (selectedFile == null) "Seleccionar" else "Cambiar")
            }

            // Botón para limpiar la selección actual y volver a la del servidor
            if (selectedFile != null) {
                OutlinedButton(onClick = { onFileSelected(null) }) {
                    Text("Revertir")
                }
                OutlinedButton(onClick = { onConfirm() }) {
                    Text("Confirmar")
                }
            }
        }
    }
}
