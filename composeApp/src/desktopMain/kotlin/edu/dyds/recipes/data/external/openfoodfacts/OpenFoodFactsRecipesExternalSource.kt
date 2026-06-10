package edu.dyds.recipes.data.external.openfoodfacts

import edu.dyds.recipes.data.external.RecipeDetailExternalSource
import edu.dyds.recipes.domain.entity.Recipe

class OpenFoodFactsRecipesExternalSource : RecipeDetailExternalSource {
    override suspend fun getRecipeById(id: String): Recipe? {
        return Recipe(
            id = id,
            name = "Healthy Bowl",
            description = "Nutritious recipe",
            ingredients = listOf("rice", "vegetables", "protein"),
            instructions = "Mix and serve",
            image = "",
            servings = 1,
            prepTime = 5,
            cookTime = 10,
            calories = 350,
            rating = 4.2
        )
    }
}

