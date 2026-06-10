package edu.dyds.recipes.data.external

import edu.dyds.recipes.domain.entity.Recipe

interface RecipeDetailExternalSource {
    suspend fun getRecipeById(id: String): Recipe?
}

interface PopularRecipesExternalSource {
    suspend fun getPopularRecipes(): List<Recipe>
}

