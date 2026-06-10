package edu.dyds.recipes.domain.entity

data class WeeklyPlan(
    val week: Int,
    val recipes: Map<String, Recipe>
)

