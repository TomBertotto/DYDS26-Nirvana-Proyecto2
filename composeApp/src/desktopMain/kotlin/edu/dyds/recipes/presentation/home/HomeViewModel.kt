package edu.dyds.recipes.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.recipes.domain.usecase.GetPopularRecipesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getPopularRecipesUseCase: GetPopularRecipesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipesUiState())
    val uiState: StateFlow<RecipesUiState> = _uiState.asStateFlow()

    init {
        getAllRecipes()
    }

    fun getAllRecipes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val recipes = getPopularRecipesUseCase()
            _uiState.value = _uiState.value.copy(recipes = recipes, isLoading = false)
        }
    }
}

