package edu.dyds.recipes.data.external.proxy

import edu.dyds.recipes.data.external.RecipeDetailExternalSource
import edu.dyds.recipes.data.external.openfoodfacts.OpenFoodFactsRecipesExternalSource
import edu.dyds.recipes.data.external.utils.toDomain
import edu.dyds.recipes.domain.entity.Recipe

class OpenFoodFactsRecipeProxy(private val openFoodFactsExternalSource: OpenFoodFactsRecipesExternalSource) : RecipeDetailExternalSource{
    override suspend fun getRecipeById(name: String): Recipe? {
        return openFoodFactsExternalSource.getRecipeByName(name)?.toDomain()
    }
}
