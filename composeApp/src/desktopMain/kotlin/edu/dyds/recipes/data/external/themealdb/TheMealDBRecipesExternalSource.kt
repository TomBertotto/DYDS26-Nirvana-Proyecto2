package edu.dyds.recipes.data.external.themealdb

import edu.dyds.recipes.data.external.RecipesSearchExternalSource
import edu.dyds.recipes.domain.entity.Recipe
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TheMealDBRecipesExternalSource(private val theMealDBHttpClient: HttpClient) : RecipesSearchExternalSource {

    suspend fun getTheMealDBById(id: String): TheMealDBRemoteRecipe? {
        val response = theMealDBHttpClient
            .get("lookup.php") { parameter("i", id) }
            .body<TheMealDBResponse>()
        return response.meals?.firstOrNull()
    }

    override suspend fun getRecipesByName(name: String): List<Recipe> {
        val response = theMealDBHttpClient
            .get("search.php") { parameter("s", name) }
            .body<TheMealDBResponse>()
        return response.meals?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun getRecipesByCategory(category: String): List<Recipe> {
        val response = theMealDBHttpClient
            .get("filter.php") { parameter("c", category) }
            .body<TheMealDBResponse>()
        return response.meals?.map { it.toDomain() } ?: emptyList()
    }

    private fun TheMealDBRemoteRecipe.toDomain() = Recipe(
        id = idMeal,
        name = strMeal,
        description = strCategory.orEmpty(),
        ingredients = ingredientList(),
        instructions = strInstructions.orEmpty(),
        image = strMealThumb.orEmpty(),
        servings = 0,
        prepTime = 0,
        cookTime = 0,
        calories = 0,
        rating = 0.0
    )
}
