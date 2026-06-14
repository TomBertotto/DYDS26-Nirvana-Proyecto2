package edu.dyds.recipes.data.external

import edu.dyds.recipes.data.external.utils.ingredientNameFrom
import edu.dyds.recipes.data.external.utils.ingredientWeightMultiplier
import edu.dyds.recipes.domain.entity.Recipe
import kotlin.math.roundToInt

class RecipeExternalSourceBroker(
    private val openFoodFactsRecipeSource: RecipeDetailExternalSource,
    private val themealdbRecipeSource: RecipeDetailExternalSource
) : RecipeDetailExternalSource {

    override suspend fun getRecipeById(id: String): Recipe? {
        val themealdbRecipe = runCatching { themealdbRecipeSource.getRecipeById(id) }.getOrNull()

        return themealdbRecipe?.let { completeRecipeWithIngredientCalories(it) }
            ?: runCatching { openFoodFactsRecipeSource.getRecipeById(id) }.getOrNull()
                ?.let { it.copy(description = "OpenFoodFacts: ${it.description}") }
    }

    private suspend fun completeRecipeWithIngredientCalories(recipe: Recipe): Recipe {
        val ingredientCalories = recipe.ingredients
            .distinct()
            .sumOf { calorieContributionFrom(it) }

        return recipe.copy(
            description = "TheMealDB: ${recipe.description}",
            calories = ingredientCalories
        )
    }

    private suspend fun calorieContributionFrom(ingredient: String): Int {
        val ingredientName = ingredientNameFrom(ingredient) ?: return 0
        val caloriesPer100g = runCatching {
            openFoodFactsRecipeSource.getRecipeById(ingredientName)?.calories ?: 0
        }.getOrDefault(0)

        return (caloriesPer100g * ingredientWeightMultiplier(ingredient)).roundToInt()
    }
}
