package edu.dyds.recipes.data.local

import edu.dyds.recipes.domain.entity.Recipe
import edu.dyds.recipes.domain.entity.WeeklyPlan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class WeeklyPlanFileLocalDataSource(
    private val file: File,
    private val json: Json
) : WeeklyPlanLocalDataSource {

    override suspend fun saveWeeklyPlan(weeklyPlan: WeeklyPlan) = withContext(Dispatchers.IO) {
        file.parentFile?.mkdirs()
        file.writeText(json.encodeToString(weeklyPlan.toEntity()))
    }

    override suspend fun getWeeklyPlan(): WeeklyPlan? = withContext(Dispatchers.IO) {
        if (!file.exists()) return@withContext null
        runCatching {
            json.decodeFromString<WeeklyPlanEntity>(file.readText()).toDomain()
        }.getOrNull()
    }

    private fun WeeklyPlan.toEntity() = WeeklyPlanEntity(
        recipes = recipes.mapValues { (_, recipe) -> recipe.toEntity() }
    )

    private fun WeeklyPlanEntity.toDomain() = WeeklyPlan(
        recipes = recipes.mapValues { (_, recipe) -> recipe.toDomain() }
    )

    private fun Recipe.toEntity() = RecipeEntity(
        id = id,
        name = name,
        description = description,
        ingredients = ingredients,
        instructions = instructions,
        image = image,
        servings = servings,
        prepTime = prepTime,
        cookTime = cookTime,
        calories = calories,
        rating = rating
    )

    private fun RecipeEntity.toDomain() = Recipe(
        id = id,
        name = name,
        description = description,
        ingredients = ingredients,
        instructions = instructions,
        image = image,
        servings = servings,
        prepTime = prepTime,
        cookTime = cookTime,
        calories = calories,
        rating = rating
    )
}
