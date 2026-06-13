package edu.dyds.recipes.domain.usecase

import edu.dyds.recipes.domain.entity.QualifiedRecipe

interface GetDefaultRecipesUseCase {
    suspend operator fun invoke(): List<QualifiedRecipe>
}
