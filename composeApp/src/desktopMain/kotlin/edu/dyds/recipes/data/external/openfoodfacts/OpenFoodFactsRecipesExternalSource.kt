package edu.dyds.recipes.data.external.openfoodfacts

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders

class OpenFoodFactsRecipesExternalSource(private val openFoodFactsHttpClient: HttpClient) {

    suspend fun getRecipeById(id: String): OpenFoodFactsRemoteProduct? {
         val response = openFoodFactsHttpClient.get("https://world.openfoodfacts.org/api/v2/product/$id.json") {

                header(HttpHeaders.UserAgent, "MiAppRecetas/1.0 (tu_correo@ejemplo.com)")

                parameter("fields", "product_name,brands,nutriscore_grade,ingredients_text")
            }

         return response.body<OpenFoodFactsRemoteProduct>()

    }
}