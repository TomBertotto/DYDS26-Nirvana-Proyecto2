package edu.dyds.recipes.data.local

import kotlinx.serialization.Serializable

@Serializable
data class WeeklyPlanEntity(
    val recipes: Map<String, RecipeEntity>
)

@Serializable
data class RecipeEntity(
    val id: String,
    val name: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: String,
    val image: String,
    val servings: Int,
    val prepTime: Int,
    val cookTime: Int,
    val calories: Int,
    val rating: Double
)
