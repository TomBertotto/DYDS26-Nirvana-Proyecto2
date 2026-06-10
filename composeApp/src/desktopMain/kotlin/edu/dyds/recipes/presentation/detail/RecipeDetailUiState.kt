package edu.dyds.recipes.presentation.detail

import edu.dyds.recipes.domain.entity.Recipe

data class RecipeDetailUiState(
    val recipe: Recipe? = null,
    val isLoading: Boolean = false
)

