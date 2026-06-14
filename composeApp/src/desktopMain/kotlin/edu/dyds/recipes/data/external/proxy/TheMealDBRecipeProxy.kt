package edu.dyds.recipes.data.external.proxy

import edu.dyds.recipes.data.external.RecipeDetailExternalSource
import edu.dyds.recipes.data.external.themealdb.TheMealDBRecipesExternalSource
import edu.dyds.recipes.data.external.utils.toDomain
import edu.dyds.recipes.domain.entity.Recipe

class TheMealDBRecipeProxy(private val theMealDBExternalSource: TheMealDBRecipesExternalSource) : RecipeDetailExternalSource {

    override suspend fun getRecipeById(id: String): Recipe? {
        return theMealDBExternalSource.getTheMealDBById(id)?.toDomain()
    }
}
