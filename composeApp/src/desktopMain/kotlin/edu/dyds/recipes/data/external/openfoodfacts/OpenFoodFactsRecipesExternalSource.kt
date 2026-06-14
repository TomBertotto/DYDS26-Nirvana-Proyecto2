package edu.dyds.recipes.data.external.openfoodfacts

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders

class OpenFoodFactsRecipesExternalSource(private val openFoodFactsHttpClient: HttpClient) {

    suspend fun getRecipeByName(name: String): OpenFoodFactsRemoteProduct? {
         val response = openFoodFactsHttpClient.get("https://world.openfoodfacts.org/cgi/search.pl") {

                header(HttpHeaders.UserAgent, "MiAppRecetas/1.0 (tu_correo@ejemplo.com)")

                parameter("search_terms", name)
                parameter("search_simple", 1)
                parameter("action", "process")
                parameter("json", 1)
                parameter("page_size", 1)
                parameter("fields", "code,product_name,generic_name,ingredients_text,image_url,serving_quantity,preparation_time,nutriments,nutriscore_grade")
            }

         return response.body<OpenFoodFactsSearchResponse>().products.firstOrNull()

    }
}
