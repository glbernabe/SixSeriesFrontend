package org.six.series.ui.components.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.six.series.ProfileBlack
import org.six.series.ProfileBlue
import org.six.series.ProfileGray
import org.six.series.ProfileGreen
import org.six.series.ProfilePink
import org.six.series.ProfilePurple
import org.six.series.ProfileRed
import org.six.series.ProfileYellow
import org.six.series.profileButtonColors
import org.six.series.ui.components.viewmodels.ProfileUiState
import org.six.series.ui.components.viewmodels.ProfileViewModel

// Predefined palette with name and color value
data class ColorOption(val name: String, val color: Color, val hexLong: Long)

val profileColorPalette = listOf(
    ColorOption("Gris",     Color(0xFF6A6A69), 0xFF6A6A69L),
    ColorOption("Rosa",     Color(0xFFE2A9F1), 0xFFE2A9F1L),
    ColorOption("Rojo",     Color(0xFFFF3131), 0xFFFF3131L),
    ColorOption("Azul",     Color(0xFF004AAD), 0xFF004AADL),
    ColorOption("Morado",   Color(0xFFCE16FF), 0xFFCE16FFL),
    ColorOption("Negro",    Color(0xFF1A1A1A), 0xFF1A1A1AL),
    ColorOption("Amarillo", Color(0xFFFFDE59), 0xFFFFDE59L),
    ColorOption("Verde",    Color(0xFF7ED957), 0xFF7ED957L),
)

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val saveSuccess by viewModel.saveSuccess.collectAsState()
    val scope = rememberCoroutineScope()

    // Estados locales de la interfaz de usuario
    var nameField by remember { mutableStateOf("") }

    // Estado local para rastrear qué color está seleccionado en la vista previa (por defecto Gris)
    var selectedColorLong by remember { mutableStateOf(0xFF6A6A69L) }

    // Transformamos el Long seleccionado en un objeto Color de Compose para alimentar la UI dinámicamente
    val currentPreviewColor = remember(selectedColorLong) { Color(selectedColorLong) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    // Sincronizamos los estados locales cuando el perfil carga por primera vez desde el servidor
    LaunchedEffect(uiState) {
        if (uiState is ProfileUiState.Success) {
            val profile = (uiState as ProfileUiState.Success).profile

            nameField = profile.name

            // Si el perfil ya tiene un color guardado en la Base de Datos, inicializamos el estado local con él
            profile.profileColor?.let { hex ->
                runCatching {
                    // Convertimos el String Hex de la BD (#RRGGBB) de vuelta a Long para Compose
                    val colorLong = hex.removePrefix("#").toLong(16) or 0xFF000000L
                    selectedColorLong = colorLong
                }
            }
        }
    }

    // Show snackbar on save
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(saveSuccess) {
        saveSuccess?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissSaveMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->
        when (uiState) {
            is ProfileUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            is ProfileUiState.NoProfile -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text("No tienes ningún perfil aún", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                            color = Color(0xFFE6E1E5))
                        var nombre by remember { mutableStateOf("") }
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre del perfil") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = { viewModel.createProfile(nombre) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = profileButtonColors()
                        ) { Text("Crear perfil") }
                    }
                }
            }
            is ProfileUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 32.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(28.dp)
                ) {
                    // CAMBIO AQUÍ: El título se queda estático con el color primario actual guardado, sin bailar con la paleta activa
                    Text(
                        "Mi Perfil",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // ── Name Section ──
                    Card(
                        modifier = Modifier.width(300.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("Nombre del perfil", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color(0xFFE6E1E5))

                            OutlinedTextField(
                                value = nameField,
                                onValueChange = { nameField = it },
                                label = { Text("Nombre") },
                                modifier = Modifier
                                    .width(192.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    focusedTextColor = Color(0xFFE6E1E5),
                                    unfocusedTextColor = Color(0xFFE6E1E5),
                                    unfocusedBorderColor = Color(0xFF444444),
                                    unfocusedLabelColor = Color(0xFF888888)
                                )
                            )
                        }
                    }

                    // ── Color Palette Section ──
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text("Color del perfil", fontWeight = FontWeight.SemiBold, fontSize = 16.sp,
                                color = Color(0xFFE6E1E5))
                            Text(
                                "El color que elijas teñirá toda la interfaz de la app",
                                fontSize = 18.sp,
                                color = Color(0xFFE6E1E5)
                            )

                            // Color grid (2 rows x 4 cols)
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                profileColorPalette.chunked(4).forEach { row ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        row.forEach { option ->
                                            ColorSwatch(
                                                option = option,
                                                isSelected = selectedColorLong == option.hexLong,
                                                onClick = {
                                                    selectedColorLong = option.hexLong
                                                },
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }

                            // Preview chip
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Vista previa:", fontSize = 13.sp,
                                    color = Color(0xFFE6E1E5))
                                Box(
                                    modifier = Modifier
                                        .height(28.dp)
                                        .background(currentPreviewColor, RoundedCornerShape(14.dp)) // Este sigue cambiando para mostrar la vista previa
                                        .padding(horizontal = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "Botón de ejemplo",
                                        color = currentPreviewColor.contrastingTextColor(),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // ── Save Button ──
                    Button(
                        onClick = { viewModel.saveChanges(nameField, selectedColorLong) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = profileButtonColors()
                    ) {
                        Text("Guardar cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            is ProfileUiState.Error -> {
                val msg = (uiState as ProfileUiState.Error).message
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(msg, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.loadProfile() }, colors = profileButtonColors()) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColorSwatch(
    option: ColorOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Transparent,
        animationSpec = tween(200)
    )
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(option.color)
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) Color.White else Color.Gray.copy(alpha = 0.4f),
                    shape = CircleShape
                )
                .clickable(onClick = onClick)
                .pointerHoverIcon(PointerIcon.Hand),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Text("✓", color = option.color.contrastingTextColor(), fontWeight = FontWeight.Bold)
            }
        }
        Text(
            option.name,
            fontSize = 14.sp,
            color = Color(0xFFCAC4D0)
        )
    }
}

private fun Color.contrastingTextColor(): Color =
    if (this.luminance() > 0.5f) Color.Black else Color.White