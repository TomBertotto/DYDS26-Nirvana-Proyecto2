package edu.dyds.recipes.domain.qualifier

import edu.dyds.recipes.domain.entity.QualifiedRecipe
import edu.dyds.recipes.domain.entity.Recipe

class RecipeQualifier {
    fun qualifyRecipes(recipes: List<Recipe>): List<QualifiedRecipe> {
        return recipes
            .map { QualifiedRecipe(it, isGoodRecipe = it.rating >= GOOD_RATING_THRESHOLD) }
            .sortedByDescending { it.recipe.rating }
    }

    companion object {
        private const val GOOD_RATING_THRESHOLD = 4.0
    }
}

