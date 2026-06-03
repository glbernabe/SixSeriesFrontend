package org.six.series.ui.components.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import org.six.series.ui.components.screens.admin.genre.AdminGenreCard
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.six.series.application.usecases.genre.AddGenreUseCase
import org.six.series.application.usecases.genre.DeleteGenreUseCase
import org.six.series.application.usecases.genre.GetGenresUseCase
import org.six.series.application.usecases.genre.UpdateGenreUseCase
import org.six.series.model.genre.Genre

// Un modelo local o mock estructurado igual que tu clase Genre real
data class MockAdminGenre(val name: String)

@Composable
fun AdminGenresSubScreen(
    getAllGenresUseCase: GetGenresUseCase,
    addGenreUseCase: AddGenreUseCase,
    updateGenreUseCase: UpdateGenreUseCase,
    deleteGenreUseCase: DeleteGenreUseCase
) {
    var genresList by remember { mutableStateOf<List<Genre>>(emptyList()) }
    var selectedGenre by remember { mutableStateOf<Genre?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // Carga inicial de datos reales
    LaunchedEffect(Unit) {
        getAllGenresUseCase().onSuccess { genresList = it }
    }

    val baseColors = listOf(
        Color(0xFF004D40), Color(0xFF3E2723), Color(0xFF01579B),
        Color(0xFF00332A), Color(0xFF310000), Color(0xFF4A148C)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Géneros del Sistema",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 44.sp
                    ),
                    color = Color.White
                )
                Text(
                    text = "Sección de control visual de las categorías asignables a películas y series.",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            }

            Button(
                onClick = { showCreateDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(50.dp).pointerHoverIcon(PointerIcon.Hand)
            ) {
                Text("+ Nuevo Género", fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 220.dp),
            contentPadding = PaddingValues(bottom = 62.dp),
            verticalArrangement = Arrangement.spacedBy(50.dp),
            horizontalArrangement = Arrangement.spacedBy(64.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(genresList) { index, genre ->
                val baseColor = baseColors[index % baseColors.size]

                AdminGenreCard(
                    genreName = genre.name,
                    topColor = baseColor,
                    onGenreClick = {
                        selectedGenre = genre
                    }
                )
            }
        }
    }

    // ── DIÁLOGO: CREAR NUEVO GÉNERO ─────────────────────────────────────────
    if (showCreateDialog) {
        var newGenreName by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            containerColor = Color(0xFF090909),
            title = { Text("Añadir Categoría", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black) },
            text = {
                OutlinedTextField(
                    value = newGenreName,
                    onValueChange = { newGenreName = it },
                    label = { Text("Nombre del género") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.DarkGray
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newGenreName.isNotBlank()) {
                            scope.launch {
                                addGenreUseCase(Genre(id = null, name = newGenreName)).onSuccess {
                                    getAllGenresUseCase().onSuccess { genresList = it }
                                    showCreateDialog = false
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Insertar", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancelar", color = Color.LightGray)
                }
            }
        )
    }

    // ── DIÁLOGO: MODIFICAR / BORRAR GÉNERO EXISTENTE ────────────────────────
    selectedGenre?.let { currentGenre ->
        var editGenreName by remember(currentGenre) { mutableStateOf(currentGenre.name) }

        AlertDialog(
            onDismissRequest = { selectedGenre = null },
            containerColor = Color(0xFF090909),
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Gestionar Género", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black)
                    TextButton(
                        onClick = {
                            currentGenre.id?.let { safeId ->
                                scope.launch {
                                    deleteGenreUseCase(safeId).onSuccess {
                                        getAllGenresUseCase().onSuccess { genresList = it }
                                        selectedGenre = null
                                    }
                                }
                            }
                        }
                    ) {
                        Text("Borrar 🗑", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                }
            },
            text = {
                OutlinedTextField(
                    value = editGenreName,
                    onValueChange = { editGenreName = it },
                    label = { Text("Editar nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.DarkGray
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editGenreName.isNotBlank()) {
                            scope.launch {
                                val updated = currentGenre.copy(name = editGenreName)
                                updateGenreUseCase(updated).onSuccess {
                                    getAllGenresUseCase().onSuccess { genresList = it }
                                    selectedGenre = null
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Guardar Cambios", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedGenre = null }) {
                    Text("Cancelar", color = Color.LightGray)
                }
            }
        )
    }
}