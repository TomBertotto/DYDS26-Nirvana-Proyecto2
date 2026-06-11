package edu.dyds.recipes.data.external.openfoodfacts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFoodFactsResponse(
    @SerialName("product") val product: OpenFoodFactsRemoteProduct?
)

@Serializable
data class OpenFoodFactsRemoteProduct(
    @SerialName("id")                   val id: String,
    @SerialName("product_name")         val productName: String?,
    @SerialName("generic_name")         val genericName: String?,
    @SerialName("ingredients_text")     val ingredientsText: String?,
    @SerialName("image_url")            val imageUrl: String?,
    @SerialName("serving_quantity")     val servingQuantity: String?,
    @SerialName("preparation_time")     val preparationTime: String?,
    @SerialName("nutriments")           val nutriments: OpenFoodFactsNutriments?,
    @SerialName("ecoscore_grade")       val ecoscoreGrade: String?,
    @SerialName("nutriscore_grade")     val nutriscoreGrade: String?,
)

@Serializable
data class OpenFoodFactsNutriments(
    @SerialName("energy-kcal_serving") val energyKcalServing: Double?,
    @SerialName("energy-kcal_100g")    val energyKcal100g: Double?,
)
