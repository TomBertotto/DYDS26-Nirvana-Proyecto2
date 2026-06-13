package edu.dyds.recipes.domain.usecase

import edu.dyds.recipes.domain.entity.Recipe
import edu.dyds.recipes.domain.entity.WeeklyPlan
import edu.dyds.recipes.domain.repository.WeeklyPlanRepository

class AddRecipeToWeeklyPlanUseCaseImpl(
    private val repository: WeeklyPlanRepository
) : AddRecipeToWeeklyPlanUseCase {
    override suspend fun invoke(recipe: Recipe) {
        val currentPlan = repository.getWeeklyPlan() ?: WeeklyPlan(emptyMap())
        val updatedRecipes = currentPlan.recipes + (recipe.id to recipe)
        repository.saveWeeklyPlan(WeeklyPlan(updatedRecipes))
    }
}
