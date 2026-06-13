package edu.dyds.recipes.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.recipes.domain.usecase.AddRecipeToWeeklyPlanUseCase
import edu.dyds.recipes.domain.usecase.GetRecipeDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase,
    private val addRecipeToWeeklyPlanUseCase: AddRecipeToWeeklyPlanUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    fun getRecipeDetail(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val recipe = getRecipeDetailsUseCase(id)
            _uiState.value = _uiState.value.copy(recipe = recipe, isLoading = false)
        }
    }

    fun addToWeeklyPlan() {
        val recipe = _uiState.value.recipe ?: return
        viewModelScope.launch {
            addRecipeToWeeklyPlanUseCase(recipe)
        }
    }
}
