package edu.dyds.recipes.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.recipes.domain.entity.QualifiedRecipe
import edu.dyds.recipes.domain.entity.Recipe
import edu.dyds.recipes.domain.usecase.AddRecipeToWeeklyPlanUseCase
import edu.dyds.recipes.domain.usecase.GetDefaultRecipesUseCase
import edu.dyds.recipes.domain.usecase.SearchRecipesByCategoryUseCase
import edu.dyds.recipes.domain.usecase.SearchRecipesByNameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getDefaultRecipesUseCase: GetDefaultRecipesUseCase,
    private val searchRecipesByNameUseCase: SearchRecipesByNameUseCase,
    private val searchRecipesByCategoryUseCase: SearchRecipesByCategoryUseCase,
    private val addRecipeToWeeklyPlanUseCase: AddRecipeToWeeklyPlanUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipesUiState())
    val uiState: StateFlow<RecipesUiState> = _uiState.asStateFlow()

    init {
        loadDefaultRecipes()
    }

    fun loadDefaultRecipes() {
        loadRecipes { getDefaultRecipesUseCase() }
    }

    fun searchByName(name: String) {
        loadRecipes { searchRecipesByNameUseCase(name) }
    }

    fun searchByCategory(category: String) {
        loadRecipes { searchRecipesByCategoryUseCase(category) }
    }

    fun addToWeeklyPlan(recipe: Recipe) {
        viewModelScope.launch {
            addRecipeToWeeklyPlanUseCase(recipe)
        }
    }

    private fun loadRecipes(block: suspend () -> List<QualifiedRecipe>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val recipes = block()
            _uiState.value = _uiState.value.copy(recipes = recipes, isLoading = false)
        }
    }
}
