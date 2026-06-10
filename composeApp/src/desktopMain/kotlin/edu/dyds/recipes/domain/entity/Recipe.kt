package edu.dyds.recipes.domain.entity

data class Recipe(
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

data class QualifiedRecipe(val recipe: Recipe, val isGoodRecipe: Boolean)

