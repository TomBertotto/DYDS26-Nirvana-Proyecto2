package edu.dyds.recipes.data.external

import edu.dyds.recipes.domain.entity.Recipe

interface RecipeDetailExternalSource {
    suspend fun getRecipeById(id: String): Recipe?
}

interface RecipesSearchExternalSource {
    suspend fun getRecipesByName(name: String): List<Recipe>
    suspend fun getRecipesByCategory(category: String): List<Recipe>
}

