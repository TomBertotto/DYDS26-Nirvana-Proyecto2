package edu.dyds.recipes.data.repository

import edu.dyds.recipes.domain.entity.Recipe

interface RecipesLocalDataSource {
    suspend fun getRecipeById(id : String) : Recipe?
    suspend fun getAllRecipes() : List<Recipe>
    suspend fun saveRecipes(remoteRecipes: kotlin.collections.List<edu.dyds.recipes.domain.entity.Recipe>)

}
