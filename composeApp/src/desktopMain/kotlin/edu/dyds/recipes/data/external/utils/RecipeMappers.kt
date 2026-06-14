package edu.dyds.recipes.data.external.utils

import edu.dyds.recipes.data.external.openfoodfacts.OpenFoodFactsRemoteProduct
import edu.dyds.recipes.data.external.themealdb.TheMealDBRemoteRecipe
import edu.dyds.recipes.domain.entity.Recipe

fun OpenFoodFactsRemoteProduct.toDomain(): Recipe {
    val ingredients = ingredientsText
        ?.split(",", ";")
        ?.map { it.trim() }
        ?.filter { it.isNotBlank() }
        ?: emptyList()

    val calories = nutriments?.energyKcal100g
        ?: nutriments?.energyKcalServing
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
        id = id,
        name = productName.orEmpty().ifBlank { genericName.orEmpty() },
        description = genericName.orEmpty(),
        ingredients = ingredients,
        instructions = "",
        image = imageUrl.orEmpty(),
        servings = servingQuantity?.toIntOrNull() ?: 0,
        prepTime = preparationTime?.toIntOrNull() ?: 0,
        cookTime = 0,
        calories = calories.toInt(),
        rating = rating
    )
}

fun TheMealDBRemoteRecipe.toDomain() = Recipe(
    id = idMeal,
    name = strMeal,
    description = strCategory.orEmpty(),
    ingredients = ingredientList(),
    instructions = strInstructions.orEmpty(),
    image = strMealThumb.orEmpty(),
    servings = 0,
    prepTime = 0,
    cookTime = 0,
    calories = 0,
    rating = 0.0
)
