package edu.dyds.recipes.domain.repository

import edu.dyds.recipes.domain.entity.Recipe

interface RecipesRepository {
    suspend fun getRecipeById(id: String): Recipe?
    suspend fun getPopularRecipes(): List<Recipe>
}

