package edu.dyds.recipes.domain.repository

import edu.dyds.recipes.domain.entity.Recipe

interface RecipesRepository {
    suspend fun getRecipeById(id: String): Recipe?
    suspend fun getDefaultRecipes(): List<Recipe>
    suspend fun searchRecipesByName(name: String): List<Recipe>
    suspend fun searchRecipesByCategory(category: String): List<Recipe>
}

