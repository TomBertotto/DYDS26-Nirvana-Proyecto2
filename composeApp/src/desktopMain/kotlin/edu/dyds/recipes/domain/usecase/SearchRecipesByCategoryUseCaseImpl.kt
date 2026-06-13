package edu.dyds.recipes.domain.usecase

import edu.dyds.recipes.domain.entity.QualifiedRecipe
import edu.dyds.recipes.domain.qualifier.RecipeQualifier
import edu.dyds.recipes.domain.repository.RecipesRepository

class SearchRecipesByCategoryUseCaseImpl(
    private val repository: RecipesRepository,
    private val qualifier: RecipeQualifier
) : SearchRecipesByCategoryUseCase {
    override suspend fun invoke(category: String): List<QualifiedRecipe> {
        return qualifier.qualifyRecipes(repository.searchRecipesByCategory(category))
    }
}
