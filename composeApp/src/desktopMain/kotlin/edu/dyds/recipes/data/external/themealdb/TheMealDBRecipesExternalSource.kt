package edu.dyds.recipes.data.external.themealdb

import edu.dyds.recipes.data.external.PopularRecipesExternalSource
import edu.dyds.recipes.data.external.RecipeDetailExternalSource
import edu.dyds.recipes.domain.entity.Recipe

class TheMealDBRecipesExternalSource : RecipeDetailExternalSource, PopularRecipesExternalSource {
    override suspend fun getRecipeById(id: String): Recipe? {
        return Recipe(
            id = id,
            name = "Pasta Carbonara",
            description = "Classic Italian pasta dish",
            ingredients = listOf("pasta", "eggs", "bacon", "cheese"),
            instructions = "1. Cook pasta\n2. Mix eggs with cheese\n3. Combine",
            image = "",
            servings = 4,
            prepTime = 10,
            cookTime = 20,
            calories = 400,
            rating = 4.5
        )
    }

    override suspend fun getPopularRecipes(): List<Recipe> {
        return listOf(
            Recipe(
                id = "1",
                name = "Spaghetti Bolognese",
                description = "Italian meat sauce pasta",
                ingredients = listOf("pasta", "meat", "tomato", "oil"),
                instructions = "Cook and serve",
                image = "",
                servings = 4,
                prepTime = 15,
                cookTime = 30,
                calories = 450,
                rating = 4.7
            ),
            Recipe(
                id = "2",
                name = "Margherita Pizza",
                description = "Simple tomato and cheese pizza",
                ingredients = listOf("dough", "tomato", "cheese", "basil"),
                instructions = "Bake at 250°C",
                image = "",
                servings = 2,
                prepTime = 20,
                cookTime = 15,
                calories = 350,
                rating = 4.6
            )
        )
    }
}

