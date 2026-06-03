package org.six.series.ui.components.screens.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import org.six.series.application.usecases.content.AddContentUseCase
import org.six.series.application.usecases.content.DeleteContentUseCase
import org.six.series.application.usecases.content.GetContentUseCase
import org.six.series.application.usecases.content.UpdateContentUseCase
import org.six.series.application.usecases.genre.GetGenresUseCase
import org.six.series.application.usecases.user.GetAllUsersUseCase
import org.six.series.model.content.Content
import org.six.series.model.content.ContentType
import org.six.series.model.genre.Genre

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminContentsSubScreen(
    userRole: String = "superuser",
    getAllContentUseCase: GetContentUseCase,
    insertContentUseCase: AddContentUseCase,
    updateContentUseCase: UpdateContentUseCase,
    deleteContentUseCase: DeleteContentUseCase,
    getGenresUseCase: GetGenresUseCase
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedContent by remember { mutableStateOf<Content?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var contentsList by remember { mutableStateOf<List<Content>>(emptyList()) }
    var genresList by remember { mutableStateOf<List<Genre>>(emptyList()) }

    var newSelectedGenres by remember { mutableStateOf<List<Genre>>(emptyList()) }
    var newSelectedGenresCreate by remember { mutableStateOf<List<Genre>>(emptyList()) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        getAllContentUseCase().onSuccess { contentsList = it }
        getGenresUseCase().onSuccess { genresList = it }
    }

    LaunchedEffect(selectedContent) {
        newSelectedGenres = selectedContent?.genres ?: emptyList<Genre>()
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f).fillMaxHeight()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Busca películas o series...") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.LightGray,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                Button(
                    onClick = {
                        newSelectedGenresCreate = emptyList()
                        showCreateDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(56.dp).pointerHoverIcon(PointerIcon.Hand)
                ) {
                    Text("+ Nuevo Contenido", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }

            val filteredContents = contentsList.filter { it.title.contains(searchQuery, ignoreCase = true) }

            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                if (filteredContents.isEmpty() && searchQuery.isNotBlank()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No tenemos este título en la base de datos", color = Color.Gray, fontSize = 16.sp)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 200.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredContents) { contentItem ->
                            AdminPosterContentCard(
                                content = contentItem,
                                onClick = { selectedContent = contentItem }
                            )
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = selectedContent != null,
            modifier = Modifier.width(460.dp).fillMaxHeight()
        ) {
            if (userRole != "superuser") {
                Box(Modifier.fillMaxSize().background(Color(0xFF1A1A1A)), contentAlignment = Alignment.Center) {
                    Text("No tienes permisos para editar contenido.", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            } else {
                selectedContent?.let { currentItem ->
                    var editTitle by remember(currentItem) { mutableStateOf(currentItem.title) }
                    var editDesc by remember(currentItem) { mutableStateOf(currentItem.description ?: "") }
                    var editDuration by remember(currentItem) { mutableStateOf(currentItem.duration?.toString() ?: "") }
                    var editAgeRating by remember(currentItem) { mutableStateOf(currentItem.ageRating) }
                    var editType by remember(currentItem) { mutableStateOf(currentItem.type) }
                    var editUploadDate by remember(currentItem) { mutableStateOf(currentItem.uploadDate ?: "") }
                    var editReleaseDate by remember(currentItem) { mutableStateOf(currentItem.releaseDate ?: "") }
                    var editCoverUrl by remember(currentItem) { mutableStateOf(currentItem.coverURL ?: "") }
                    var editVideoUrl by remember(currentItem) { mutableStateOf(currentItem.videoURL ?: "") }
                    var editLogoUrl by remember(currentItem) { mutableStateOf(currentItem.logoURL ?: "") }
                    var editPortraitUrl by remember(currentItem) { mutableStateOf(currentItem.portraitURL ?: "") }

                    val inputFieldColors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = Color.LightGray,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.DarkGray
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF090909)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxSize().padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp).fillMaxSize()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Modificar Registro", fontWeight = FontWeight.Black, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    TextButton(
                                        onClick = {
                                            currentItem.id?.let { safeId ->
                                                scope.launch {
                                                    deleteContentUseCase(safeId).onSuccess {
                                                        getAllContentUseCase().onSuccess { contentsList = it }
                                                        selectedContent = null
                                                    }
                                                }
                                            }
                                        }
                                    ) { Text("Borrar 🗑", color = Color.Red) }
                                    TextButton(onClick = { selectedContent = null }) { Text("Cerrar ✕", color = Color.Gray) }
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState())
                                    .padding(vertical = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                OutlinedTextField(
                                    value = editTitle,
                                    onValueChange = { editTitle = it },
                                    label = { Text("title (varchar 200)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = inputFieldColors
                                )

                                OutlinedTextField(
                                    value = editDesc,
                                    onValueChange = { editDesc = it },
                                    label = { Text("description (varchar 255 NULL)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    maxLines = 3,
                                    colors = inputFieldColors
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                                    OutlinedTextField(
                                        value = editDuration,
                                        onValueChange = { editDuration = it },
                                        label = { Text("duration (time)") },
                                        modifier = Modifier.weight(1f),
                                        placeholder = { Text("HH:MM:SS", color = Color.Gray) },
                                        colors = inputFieldColors
                                    )
                                    OutlinedTextField(
                                        value = editAgeRating,
                                        onValueChange = { editAgeRating = it },
                                        label = { Text("ageRating (varchar)") },
                                        modifier = Modifier.weight(1f),
                                        colors = inputFieldColors
                                    )
                                }

                                Text("type (enum):", fontWeight = FontWeight.Bold, color = Color.LightGray, fontSize = 12.sp)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                    ContentType.entries.forEach { typeOpt ->
                                        val isSelected = editType == typeOpt
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color(0xFF141414))
                                                .clickable { editType = typeOpt }
                                                .pointerHoverIcon(PointerIcon.Hand)
                                                .padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(typeOpt.name.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray)
                                        }
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                                    OutlinedTextField(
                                        value = editReleaseDate,
                                        onValueChange = { editReleaseDate = it },
                                        label = { Text("releaseDate (date)") },
                                        modifier = Modifier.weight(1f),
                                        placeholder = { Text("YYYY-MM-DD", color = Color.Gray) },
                                        colors = inputFieldColors
                                    )
                                    OutlinedTextField(
                                        value = editUploadDate,
                                        onValueChange = { editUploadDate = it },
                                        label = { Text("uploadDate (date)") },
                                        modifier = Modifier.weight(1f),
                                        placeholder = { Text("YYYY-MM-DD", color = Color.Gray) },
                                        colors = inputFieldColors
                                    )
                                }

                                OutlinedTextField(value = editVideoUrl, onValueChange = { editVideoUrl = it }, label = { Text("videoUrl (varchar 255)") }, modifier = Modifier.fillMaxWidth(), colors = inputFieldColors)
                                OutlinedTextField(value = editCoverUrl, onValueChange = { editCoverUrl = it }, label = { Text("coverUrl (varchar 255)") }, modifier = Modifier.fillMaxWidth(), colors = inputFieldColors)
                                OutlinedTextField(value = editLogoUrl, onValueChange = { editLogoUrl = it }, label = { Text("logoURL (varchar 255)") }, modifier = Modifier.fillMaxWidth(), colors = inputFieldColors)
                                OutlinedTextField(value = editPortraitUrl, onValueChange = { editPortraitUrl = it }, label = { Text("portraitURL (varchar 255)") }, modifier = Modifier.fillMaxWidth(), colors = inputFieldColors)

                                Spacer(Modifier.height(8.dp))
                                Text("Géneros Vinculados (Relacional)", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)

                                Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                                    LazyVerticalGrid(
                                        columns = GridCells.Adaptive(minSize = 100.dp),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        items(genresList) { genre ->
                                            val isAssigned = newSelectedGenres.any { it.id == genre.id }

                                            FilterChip(
                                                selected = isAssigned,
                                                onClick = {
                                                    newSelectedGenres = if (isAssigned) {
                                                        newSelectedGenres.filterNot { it.id == genre.id }
                                                    } else {
                                                        newSelectedGenres + genre
                                                    }
                                                },
                                                label = {
                                                    Text(
                                                        text = genre.name,
                                                        fontSize = 11.sp,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    val parsedDuration = try {
                                        if (editDuration.isNotBlank()) LocalTime.parse(editDuration) else null
                                    } catch (e: Exception) {
                                        throw e
                                    }

                                    val updatedContent = currentItem.copy(
                                        title = editTitle,
                                        description = editDesc,
                                        duration = parsedDuration,
                                        ageRating = editAgeRating,
                                        type = editType,
                                        releaseDate = editReleaseDate.ifBlank { null },
                                        uploadDate = editUploadDate.ifBlank { null },
                                        videoURL = editVideoUrl.ifBlank { null },
                                        coverURL = editCoverUrl.ifBlank { null },
                                        logoURL = editLogoUrl.ifBlank { null },
                                        portraitURL = editPortraitUrl.ifBlank { null },
                                        genres = newSelectedGenres
                                    )
                                    scope.launch {
                                        updateContentUseCase(updatedContent).onSuccess {
                                            getAllContentUseCase().onSuccess { contentsList = it }
                                            selectedContent = null
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().height(50.dp).pointerHoverIcon(PointerIcon.Hand),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Guardar Cambios en la BD", fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        var newTitle by remember { mutableStateOf("") }
        var newDesc by remember { mutableStateOf("") }
        var newDuration by remember { mutableStateOf("") }
        var newAgeRating by remember { mutableStateOf("") }
        var newType by remember { mutableStateOf(ContentType.Movie) }
        var newReleaseDate by remember { mutableStateOf("") }
        var newUploadDate by remember { mutableStateOf("") }
        var newVideoUrl by remember { mutableStateOf("") }
        var newCoverUrl by remember { mutableStateOf("") }
        var newLogoUrl by remember { mutableStateOf("") }
        var newPortraitUrl by remember { mutableStateOf("") }

        val createFieldColors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = Color.LightGray,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.DarkGray
        )

        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            containerColor = Color(0xFF090909),
            title = {
                Text(
                    "Dar de Alta Nueva Producción",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .width(460.dp)
                        .height(550.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(value = newTitle, onValueChange = { newTitle = it }, label = { Text("title (varchar 200)") }, modifier = Modifier.fillMaxWidth(), colors = createFieldColors)

                    OutlinedTextField(value = newDesc, onValueChange = { newDesc = it }, label = { Text("description (varchar 255 NULL)") }, modifier = Modifier.fillMaxWidth(), maxLines = 3, colors = createFieldColors)

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(value = newDuration, onValueChange = { newDuration = it }, label = { Text("duration (time)") }, modifier = Modifier.weight(1f), placeholder = { Text("HH:MM:SS", color = Color.Gray) }, colors = createFieldColors)
                        OutlinedTextField(value = newAgeRating, onValueChange = { newAgeRating = it }, label = { Text("ageRating (varchar)") }, modifier = Modifier.weight(1f), colors = createFieldColors)
                    }

                    Text("type (enum):", fontWeight = FontWeight.Bold, color = Color.LightGray, fontSize = 12.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        ContentType.values().forEach { typeOpt ->
                            val isSelected = newType == typeOpt
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color(0xFF141414))
                                    .clickable { newType = typeOpt }
                                    .pointerHoverIcon(PointerIcon.Hand)
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(typeOpt.name.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray)
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(value = newReleaseDate, onValueChange = { newReleaseDate = it }, label = { Text("releaseDate (date)") }, modifier = Modifier.weight(1f), placeholder = { Text("YYYY-MM-DD", color = Color.Gray) }, colors = createFieldColors)
                        OutlinedTextField(value = newUploadDate, onValueChange = { newUploadDate = it }, label = { Text("uploadDate (date)") }, modifier = Modifier.weight(1f), placeholder = { Text("YYYY-MM-DD", color = Color.Gray) }, colors = createFieldColors)
                    }

                    OutlinedTextField(value = newVideoUrl, onValueChange = { newVideoUrl = it }, label = { Text("videoUrl (varchar 255)") }, modifier = Modifier.fillMaxWidth(), colors = createFieldColors)
                    OutlinedTextField(value = newCoverUrl, onValueChange = { newCoverUrl = it }, label = { Text("coverUrl (varchar 255)") }, modifier = Modifier.fillMaxWidth(), colors = createFieldColors)
                    OutlinedTextField(value = newLogoUrl, onValueChange = { newLogoUrl = it }, label = { Text("logoURL (varchar 255)") }, modifier = Modifier.fillMaxWidth(), colors = createFieldColors)
                    OutlinedTextField(value = newPortraitUrl, onValueChange = { newPortraitUrl = it }, label = { Text("portraitURL (varchar 255)") }, modifier = Modifier.fillMaxWidth(), colors = createFieldColors)

                    Spacer(Modifier.height(4.dp))
                    Text("Asignar Géneros (Relacional)", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)

                    Box(modifier = Modifier.fillMaxWidth().height(110.dp)) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 100.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(genresList) { genre ->
                                val isAssigned = newSelectedGenresCreate.any { it.id == genre.id }

                                FilterChip(
                                    selected = isAssigned,
                                    onClick = {
                                        newSelectedGenresCreate = if (isAssigned) {
                                            newSelectedGenresCreate.filterNot { it.id == genre.id }
                                        } else {
                                            newSelectedGenresCreate + genre
                                        }
                                    },
                                    label = {
                                        Text(
                                            text = genre.name,
                                            fontSize = 11.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedDuration = try {
                            if (newDuration.isNotBlank()) LocalTime.parse(newDuration) else null
                        } catch (e: Exception) {
                            null
                        }

                        val newContent = Content(
                            id = null,
                            title = newTitle,
                            description = newDesc,
                            duration = parsedDuration,
                            ageRating = newAgeRating,
                            type = newType,
                            releaseDate = newReleaseDate.ifBlank { null },
                            uploadDate = newUploadDate.ifBlank { null },
                            videoURL = newVideoUrl.ifBlank { null },
                            coverURL = newCoverUrl.ifBlank { null },
                            logoURL = newLogoUrl.ifBlank { null },
                            portraitURL = newPortraitUrl.ifBlank { null },
                            genres = newSelectedGenresCreate,
                            episodes = emptyList()
                        )
                        scope.launch {
                            insertContentUseCase(newContent).onSuccess {
                                getAllContentUseCase().onSuccess { contentsList = it }
                                showCreateDialog = false
                            }
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Insertar Registro en la BD", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancelar", color = Color.LightGray)
                }
            }
        )
    }
}

@Composable
private fun AdminPosterContentCard(
    content: Content,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(210.dp)
            .height(340.dp)
            .clickable { onClick() }
            .pointerHoverIcon(PointerIcon.Hand),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(3.2f)
                    .fillMaxWidth()
                    .background(Color(0xFF222222)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = content.portraitURL?.ifBlank {
                        "https://via.placeholder.com/300x450?text=No+Image"
                    },
                    contentDescription = content.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = content.title,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    color = Color.White,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = content.type.name.uppercase(),
                    style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
            }
        }
    }
}