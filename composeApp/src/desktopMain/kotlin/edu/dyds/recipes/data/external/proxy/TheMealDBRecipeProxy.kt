package edu.dyds.recipes.data.external.proxy

import edu.dyds.recipes.data.external.RecipeDetailExternalSource
import edu.dyds.recipes.data.external.themealdb.TheMealDBRecipesExternalSource
import edu.dyds.recipes.data.external.themealdb.TheMealDBRemoteRecipe
import edu.dyds.recipes.domain.entity.Recipe

class TheMealDBRecipeProxy(private val theMealDBExternalSource: TheMealDBRecipesExternalSource) : RecipeDetailExternalSource {
    override suspend fun getRecipeById(id: String): Recipe? {
        return theMealDBExternalSource.getTheMealDBById(id)?.toDomain()
    }
    private fun TheMealDBRemoteRecipe.toDomain() = Recipe(
        id          = idMeal,
        name        = strMeal,
        description = strCategory.orEmpty(),
        ingredients = ingredientList(),
        instructions = strInstructions.orEmpty(),
        image       = strMealThumb.orEmpty(),
        servings    = 0,
        prepTime    = 0,
        cookTime    = 0,
        calories    = 0,
        rating      = 0.0
    )
}
