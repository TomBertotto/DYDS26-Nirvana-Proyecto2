package edu.dyds.recipes.data.repository

import edu.dyds.recipes.data.external.RecipeDetailExternalSource
import edu.dyds.recipes.data.external.RecipesSearchExternalSource
import edu.dyds.recipes.data.local.RecipesLocalDataSource
import edu.dyds.recipes.domain.entity.Recipe
import edu.dyds.recipes.domain.repository.RecipesRepository

class RecipesRepositoryImpl(
    private val recipeDetailExternalSource: RecipeDetailExternalSource,
    private val recipesSearchExternalSource: RecipesSearchExternalSource,
    private val localDataSource: RecipesLocalDataSource
) : RecipesRepository {

    override suspend fun getRecipeById(id: String): Recipe? {
        val remoteRecipe = runCatching { recipeDetailExternalSource.getRecipeById(id) }.getOrNull()

        return remoteRecipe ?: runCatching { localDataSource.getRecipeById(id) }.getOrNull()
    }

    override suspend fun getDefaultRecipes(): List<Recipe> {
        val localRecipes = localDataSource.getAllRecipes()

        if (localRecipes.isNotEmpty()) {
            return localRecipes
        }

        val remoteRecipes = runCatching {
            recipesSearchExternalSource.getRecipesByCategory(DEFAULT_CATEGORY)
        }.getOrNull() ?: emptyList()

        if (remoteRecipes.isNotEmpty()) {
            localDataSource.saveRecipes(remoteRecipes)
        }

        return remoteRecipes
    }

    override suspend fun searchRecipesByName(name: String): List<Recipe> {
        return runCatching { recipesSearchExternalSource.getRecipesByName(name) }.getOrNull() ?: emptyList()
    }

    override suspend fun searchRecipesByCategory(category: String): List<Recipe> {
        return runCatching { recipesSearchExternalSource.getRecipesByCategory(category) }.getOrNull() ?: emptyList()
    }

    companion object {
        private const val DEFAULT_CATEGORY = "Seafood"
    }
}
