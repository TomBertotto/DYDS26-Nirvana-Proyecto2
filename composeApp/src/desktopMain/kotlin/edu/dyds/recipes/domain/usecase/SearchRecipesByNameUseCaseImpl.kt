package edu.dyds.recipes.domain.usecase

import edu.dyds.recipes.domain.entity.QualifiedRecipe
import edu.dyds.recipes.domain.qualifier.RecipeQualifier
import edu.dyds.recipes.domain.repository.RecipesRepository

class SearchRecipesByNameUseCaseImpl(
    private val repository: RecipesRepository,
    private val qualifier: RecipeQualifier
) : SearchRecipesByNameUseCase {
    override suspend fun invoke(name: String): List<QualifiedRecipe> {
        return qualifier.qualifyRecipes(repository.searchRecipesByName(name))
    }
}
