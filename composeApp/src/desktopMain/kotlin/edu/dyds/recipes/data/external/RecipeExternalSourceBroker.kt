package edu.dyds.recipes.data.external

import edu.dyds.recipes.domain.entity.Recipe

class RecipeExternalSourceBroker(
    private val openFoodFactsRecipeSource: RecipeDetailExternalSource,
    private val themealdbRecipeSource: RecipeDetailExternalSource
) : RecipeDetailExternalSource {

    override suspend fun getRecipeById(id: String): Recipe? {
        val openFoodFactsRecipe = runCatching { openFoodFactsRecipeSource.getRecipeById(id) }.getOrNull()
        val themealdbRecipe = runCatching { themealdbRecipeSource.getRecipeById(id) }.getOrNull()

        return when {
            openFoodFactsRecipe != null && themealdbRecipe != null -> mergeRecipes(openFoodFactsRecipe, themealdbRecipe)
            openFoodFactsRecipe != null -> openFoodFactsRecipe.copy(description = "OpenFoodFacts: ${openFoodFactsRecipe.description}")
            themealdbRecipe != null -> themealdbRecipe.copy(description = "TheMealDB: ${themealdbRecipe.description}")
            else -> null
        }
    }

    private fun mergeRecipes(openFoodFacts: Recipe, themealdb: Recipe): Recipe {
        return themealdb.copy(
            description = "TheMealDB: ${themealdb.description}\n\nOpenFoodFacts: ${openFoodFacts.description}",
            calories = (openFoodFacts.calories + themealdb.calories) / 2,
            rating = (openFoodFacts.rating + themealdb.rating) / 2.0
        )
    }
}

