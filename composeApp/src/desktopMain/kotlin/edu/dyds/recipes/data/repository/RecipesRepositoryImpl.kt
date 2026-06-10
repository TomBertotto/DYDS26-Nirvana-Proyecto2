package edu.dyds.recipes.data.repository

import edu.dyds.recipes.data.external.PopularRecipesExternalSource
import edu.dyds.recipes.data.external.RecipeDetailExternalSource
import edu.dyds.recipes.data.local.RecipesLocalDataSource
import edu.dyds.recipes.domain.entity.Recipe
import edu.dyds.recipes.domain.repository.RecipesRepository

class RecipesRepositoryImpl(
    private val recipeDetailExternalSource: RecipeDetailExternalSource,
    private val popularRecipesExternalSource: PopularRecipesExternalSource,
    private val localDataSource: RecipesLocalDataSource
) : RecipesRepository {

    override suspend fun getRecipeById(id: String): Recipe? {
        val remoteRecipe = runCatching { recipeDetailExternalSource.getRecipeById(id) }.getOrNull()

        return remoteRecipe ?: runCatching { localDataSource.getRecipeById(id) }.getOrNull()
    }

    override suspend fun getPopularRecipes(): List<Recipe> {
        val localRecipes = localDataSource.getAllRecipes()

        if (localRecipes.isNotEmpty()) {
            return localRecipes
        }

        val remoteRecipes = runCatching { popularRecipesExternalSource.getPopularRecipes() }.getOrNull() ?: emptyList()

        if (remoteRecipes.isNotEmpty()) {
            localDataSource.saveRecipes(remoteRecipes)
        }

        return remoteRecipes
    }
}

