package edu.dyds.countries.domain.entity

data class Country(
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

data class QualifiedCountry(val country: Country, val isGoodCountry: Boolean)
