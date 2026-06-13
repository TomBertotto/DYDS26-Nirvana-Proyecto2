package edu.dyds.recipes.domain.usecase

import edu.dyds.recipes.domain.entity.Recipe

interface AddRecipeToWeeklyPlanUseCase {
    suspend operator fun invoke(recipe: Recipe)
}
