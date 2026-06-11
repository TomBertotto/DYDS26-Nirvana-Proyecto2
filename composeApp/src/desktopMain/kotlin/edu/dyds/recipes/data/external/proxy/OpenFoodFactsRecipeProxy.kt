package edu.dyds.recipes.data.external.proxy

import edu.dyds.recipes.data.external.RecipeDetailExternalSource
import edu.dyds.recipes.data.external.openfoodfacts.OpenFoodFactsRecipesExternalSource
import edu.dyds.recipes.data.external.openfoodfacts.OpenFoodFactsRemoteProduct
import edu.dyds.recipes.domain.entity.Recipe

class OpenFoodFactsRecipeProxy(private val openFoodFactsExternalSource: OpenFoodFactsRecipesExternalSource) : RecipeDetailExternalSource{
    override suspend fun getRecipeById(id: String): Recipe? {
        return openFoodFactsExternalSource.getRecipeById(id)?.toDomain()
    }

    private fun OpenFoodFactsRemoteProduct.toDomain(): Recipe {
        val ingredients = ingredientsText
            ?.split(",", ";")
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() }
            ?: emptyList()

        val calories = nutriments?.energyKcalServing
            ?: nutriments?.energyKcal100g
            ?: 0.0

        val rating = when (nutriscoreGrade?.uppercase()) {
            "A" -> 5.0
            "B" -> 4.0
            "C" -> 3.0
            "D" -> 2.0
            "E" -> 1.0
            else -> 0.0
        }

        return Recipe(
            id           = id,
            name         = productName.orEmpty().ifBlank { genericName.orEmpty() },
            description  = genericName.orEmpty(),
            ingredients  = ingredients,
            instructions = "",
            image        = imageUrl.orEmpty(),
            servings     = servingQuantity?.toIntOrNull() ?: 0,
            prepTime     = preparationTime?.toIntOrNull() ?: 0,
            cookTime     = 0,
            calories     = calories.toInt(),
            rating       = rating
        )
    }
}