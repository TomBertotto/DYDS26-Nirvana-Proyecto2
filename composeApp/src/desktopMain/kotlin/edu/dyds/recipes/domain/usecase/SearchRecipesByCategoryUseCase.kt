package edu.dyds.recipes.domain.usecase

import edu.dyds.recipes.domain.entity.QualifiedRecipe

interface SearchRecipesByCategoryUseCase {
    suspend operator fun invoke(category: String): List<QualifiedRecipe>
}
