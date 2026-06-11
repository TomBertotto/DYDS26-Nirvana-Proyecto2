package edu.dyds.recipes.data.external.themealdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TheMealDBResponse(
    @SerialName("meals") val meals: List<TheMealDBRemoteRecipe>?
)

@Serializable
data class TheMealDBRemoteRecipe(
    @SerialName("idMeal")           val idMeal: String,
    @SerialName("strMeal")          val strMeal: String,
    @SerialName("strCategory")      val strCategory: String?,
    @SerialName("strInstructions")  val strInstructions: String?,
    @SerialName("strMealThumb")     val strMealThumb: String?,
    @SerialName("strIngredient1")   val strIngredient1: String?,
    @SerialName("strIngredient2")   val strIngredient2: String?,
    @SerialName("strIngredient3")   val strIngredient3: String?,
    @SerialName("strIngredient4")   val strIngredient4: String?,
    @SerialName("strIngredient5")   val strIngredient5: String?,
    @SerialName("strIngredient6")   val strIngredient6: String?,
    @SerialName("strIngredient7")   val strIngredient7: String?,
    @SerialName("strIngredient8")   val strIngredient8: String?,
    @SerialName("strIngredient9")   val strIngredient9: String?,
    @SerialName("strIngredient10")  val strIngredient10: String?,
    @SerialName("strIngredient11")  val strIngredient11: String?,
    @SerialName("strIngredient12")  val strIngredient12: String?,
    @SerialName("strIngredient13")  val strIngredient13: String?,
    @SerialName("strIngredient14")  val strIngredient14: String?,
    @SerialName("strIngredient15")  val strIngredient15: String?,
    @SerialName("strMeasure1")      val strMeasure1: String?,
    @SerialName("strMeasure2")      val strMeasure2: String?,
    @SerialName("strMeasure3")      val strMeasure3: String?,
    @SerialName("strMeasure4")      val strMeasure4: String?,
    @SerialName("strMeasure5")      val strMeasure5: String?,
    @SerialName("strMeasure6")      val strMeasure6: String?,
    @SerialName("strMeasure7")      val strMeasure7: String?,
    @SerialName("strMeasure8")      val strMeasure8: String?,
    @SerialName("strMeasure9")      val strMeasure9: String?,
    @SerialName("strMeasure10")     val strMeasure10: String?,
    @SerialName("strMeasure11")     val strMeasure11: String?,
    @SerialName("strMeasure12")     val strMeasure12: String?,
    @SerialName("strMeasure13")     val strMeasure13: String?,
    @SerialName("strMeasure14")     val strMeasure14: String?,
    @SerialName("strMeasure15")     val strMeasure15: String?,
) {
    /** Combina ingredientes y medidas descartando los pares vacíos. */
    fun ingredientList(): List<String> {
        val ingredients = listOf(
            strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
            strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
            strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15
        )
        val measures = listOf(
            strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
            strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
            strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15
        )
        return ingredients.zip(measures)
            .filter { (ing, _) -> !ing.isNullOrBlank() }
            .map { (ing, measure) ->
                if (measure.isNullOrBlank()) ing!! else "${measure.trim()} ${ing!!.trim()}"
            }
    }
}