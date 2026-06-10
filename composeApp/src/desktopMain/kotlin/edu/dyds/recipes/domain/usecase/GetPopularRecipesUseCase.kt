package edu.dyds.recipes.domain.usecase

import edu.dyds.recipes.domain.entity.QualifiedRecipe
import edu.dyds.recipes.domain.qualifier.RecipeQualifier
import edu.dyds.recipes.domain.repository.RecipesRepository

interface GetPopularRecipesUseCase {
    suspend operator fun invoke(): List<QualifiedRecipe>
}

internal class GetPopularRecipesUseCaseImpl(
    private val repository: RecipesRepository,
    private val qualifier: RecipeQualifier
) : GetPopularRecipesUseCase {
    override suspend fun invoke(): List<QualifiedRecipe> {
        val recipes = repository.getPopularRecipes()
        return qualifier.qualifyRecipes(recipes)
    }
}

