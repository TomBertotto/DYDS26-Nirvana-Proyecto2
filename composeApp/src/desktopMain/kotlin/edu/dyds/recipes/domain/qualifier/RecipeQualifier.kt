package edu.dyds.recipes.domain.qualifier

import edu.dyds.recipes.domain.entity.QualifiedRecipe
import edu.dyds.recipes.domain.entity.Recipe

class RecipeQualifier {
    fun qualifyRecipes(recipes: List<Recipe>): List<QualifiedRecipe> {
        return recipes
            .filter { it.rating >= 4.0 }
            .map { QualifiedRecipe(it, isGoodRecipe = true) }
            .sortedByDescending { it.recipe.rating }
    }
}

