package edu.dyds.recipes.data.external.themealdb

import edu.dyds.recipes.data.external.RecipesSearchExternalSource
import edu.dyds.recipes.data.external.utils.toDomain
import edu.dyds.recipes.domain.entity.Recipe
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TheMealDBRecipesExternalSource(private val httpClient: HttpClient) : RecipesSearchExternalSource {

    suspend fun getTheMealDBById(id: String): TheMealDBRemoteRecipe? {
        return fetchMeals(LOOKUP_ENDPOINT, ID_PARAMETER, id).firstOrNull()
    }

    override suspend fun getRecipesByName(name: String): List<Recipe> {
        return fetchRecipes(SEARCH_ENDPOINT, SEARCH_PARAMETER, name)
    }

    override suspend fun getRecipesByCategory(category: String): List<Recipe> {
        return fetchRecipes(FILTER_ENDPOINT, CATEGORY_PARAMETER, category)
    }

    private suspend fun fetchRecipes(endpoint: String, parameterName: String, parameterValue: String): List<Recipe> {
        return fetchMeals(endpoint, parameterName, parameterValue)
            .map { it.toDomain() }
    }

    private suspend fun fetchMeals(endpoint: String, parameterName: String, parameterValue: String): List<TheMealDBRemoteRecipe> {
        val response = httpClient
            .get(endpoint) { parameter(parameterName, parameterValue) }
            .body<TheMealDBResponse>()

        return response.meals.orEmpty()
    }

    private companion object {
        const val LOOKUP_ENDPOINT = "lookup.php"
        const val SEARCH_ENDPOINT = "search.php"
        const val FILTER_ENDPOINT = "filter.php"

        const val ID_PARAMETER = "i"
        const val SEARCH_PARAMETER = "s"
        const val CATEGORY_PARAMETER = "c"
    }
}
