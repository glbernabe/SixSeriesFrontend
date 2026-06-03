package org.six.series.ui.components.screens.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.six.series.application.usecases.content.*
import org.six.series.application.usecases.genre.AddContentToGenreUseCase
import org.six.series.application.usecases.genre.AddGenreUseCase
import org.six.series.application.usecases.genre.DeleteGenreUseCase
import org.six.series.application.usecases.genre.GetGenresUseCase
import org.six.series.application.usecases.genre.UpdateGenreUseCase
import org.six.series.application.usecases.user.GetAllUsersUseCase
import org.six.series.application.usecases.user.UpdateUserAccountUseCase
import org.six.series.application.usecases.user.UpdateUserStatusUseCase
import org.six.series.application.usecases.user.DeleteUserByIdUseCase
import org.six.series.model.content.Content
import org.six.series.model.content.Episode
import org.six.series.model.genre.Genre
import org.six.series.model.user.UserAccount


class AdminPanelViewModel(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getContentUseCase: GetContentUseCase,
    private val getGenresUseCase: GetGenresUseCase,
) : ViewModel() {

    private val _users = MutableStateFlow<List<UserAccount>>(emptyList())
    val users: StateFlow<List<UserAccount>> = _users.asStateFlow()

    private val _contents = MutableStateFlow<List<Content>>(emptyList())
    val contents: StateFlow<List<Content>> = _contents.asStateFlow()

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        refreshAllData()
    }

    fun refreshAllData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                getAllUsersUseCase().onSuccess { _users.value = it }
                getContentUseCase().onSuccess { _contents.value = it }
                getGenresUseCase().onSuccess { _genres.value = it }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }


}