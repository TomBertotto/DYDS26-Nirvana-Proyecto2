package edu.dyds.recipes.data.local

import edu.dyds.recipes.data.repository.RecipesLocalDataSource
import edu.dyds.recipes.domain.entity.Recipe

class RecipesLocalDataSourceImpl : RecipesLocalDataSource {
    private val recipeCache = mutableMapOf<String, Recipe>()

    override suspend fun saveRecipes(recipes: List<Recipe>) {
        recipeCache.clear()
        recipes.forEach { recipeCache[it.id] = it }
    }

    override suspend fun getRecipeById(id: String): Recipe? {
        return recipeCache[id]
    }

    override suspend fun getAllRecipes(): List<Recipe> {
        return recipeCache.values.toList()
    }
}

