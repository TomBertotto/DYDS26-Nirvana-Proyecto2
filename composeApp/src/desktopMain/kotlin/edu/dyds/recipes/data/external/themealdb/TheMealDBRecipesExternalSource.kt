package edu.dyds.recipes.data.external.themealdb

import edu.dyds.recipes.data.external.PopularRecipesExternalSource
import edu.dyds.recipes.domain.entity.Recipe
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TheMealDBRecipesExternalSource(private val theMealDBHttpClient: HttpClient) : PopularRecipesExternalSource {

    suspend fun getTheMealDBById(id: String): TheMealDBRemoteRecipe? {
        val response = theMealDBHttpClient
            .get("lookup.php") { parameter("i", id) }
            .body<TheMealDBResponse>()
        return response.meals?.firstOrNull()
    }

    private suspend fun getRecipeById(id: String): Recipe {
        val response = theMealDBHttpClient
            .get("lookup.php") { parameter("i", id) }
            .body<Recipe>()
        return response
    }

    override suspend fun getPopularRecipes(): List<Recipe> {
        val summaryResponse = theMealDBHttpClient
            .get("filter.php") { parameter("c", "Seafood") }
            .body<TheMealDBResponse>()

        val ids = summaryResponse.meals?.map { it.idMeal } ?: return emptyList()

        return ids.take(10).mapNotNull { id -> getRecipeById(id) }
    }
}