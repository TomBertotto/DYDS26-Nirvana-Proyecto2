package edu.dyds.recipes.domain.usecase

import edu.dyds.recipes.domain.entity.QualifiedRecipe
import edu.dyds.recipes.domain.qualifier.RecipeQualifier
import edu.dyds.recipes.domain.repository.RecipesRepository

interface GetDefaultRecipesUseCase {
    suspend operator fun invoke(): List<QualifiedRecipe>
}

internal class GetDefaultRecipesUseCaseImpl(
    private val repository: RecipesRepository,
    private val qualifier: RecipeQualifier
) : GetDefaultRecipesUseCase {
    override suspend fun invoke(): List<QualifiedRecipe> {
        return qualifier.qualifyRecipes(repository.getDefaultRecipes())
    }
}
