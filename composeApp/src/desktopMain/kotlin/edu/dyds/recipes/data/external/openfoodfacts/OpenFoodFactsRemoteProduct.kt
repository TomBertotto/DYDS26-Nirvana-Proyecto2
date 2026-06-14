package edu.dyds.recipes.data.external.openfoodfacts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFoodFactsResponse(
    @SerialName("product") val product: OpenFoodFactsRemoteProduct?
)

@Serializable
data class OpenFoodFactsSearchResponse(
    @SerialName("products") val products: List<OpenFoodFactsRemoteProduct> = emptyList()
)

@Serializable
data class OpenFoodFactsRemoteProduct(
    @SerialName("code")                 val id: String = "",
    @SerialName("product_name")         val productName: String? = null,
    @SerialName("generic_name")         val genericName: String? = null,
    @SerialName("ingredients_text")     val ingredientsText: String? = null,
    @SerialName("image_url")            val imageUrl: String? = null,
    @SerialName("serving_quantity")     val servingQuantity: String? = null,
    @SerialName("preparation_time")     val preparationTime: String? = null,
    @SerialName("nutriments")           val nutriments: OpenFoodFactsNutriments? = null,
    @SerialName("ecoscore_grade")       val ecoscoreGrade: String? = null,
    @SerialName("nutriscore_grade")     val nutriscoreGrade: String? = null,
)

@Serializable
data class OpenFoodFactsNutriments(
    @SerialName("energy-kcal_serving") val energyKcalServing: Double? = null,
    @SerialName("energy-kcal_100g")    val energyKcal100g: Double? = null,
)
