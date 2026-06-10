package edu.dyds.recipes.presentation.home

import edu.dyds.recipes.domain.entity.QualifiedRecipe

data class RecipesUiState(
    val recipes: List<QualifiedRecipe> = emptyList(),
    val isLoading: Boolean = false
)

