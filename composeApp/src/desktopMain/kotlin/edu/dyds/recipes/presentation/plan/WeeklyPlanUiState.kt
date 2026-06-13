package edu.dyds.recipes.presentation.plan

import edu.dyds.recipes.domain.entity.Recipe

data class WeeklyPlanUiState(
    val recipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false
)
