package edu.dyds.recipes.domain.usecase

import edu.dyds.recipes.domain.entity.Recipe
import edu.dyds.recipes.domain.repository.RecipesRepository

interface GetRecipeDetailsUseCase {
    suspend operator fun invoke(id: String): Recipe?
}

internal class GetRecipeDetailsUseCaseImpl(
    private val repository: RecipesRepository
) : GetRecipeDetailsUseCase {
    override suspend fun invoke(id: String): Recipe? {
        return repository.getRecipeById(id)
    }
}

