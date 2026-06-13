package edu.dyds.recipes.domain.usecase

import edu.dyds.recipes.domain.entity.QualifiedRecipe

interface SearchRecipesByNameUseCase {
    suspend operator fun invoke(name: String): List<QualifiedRecipe>
}
