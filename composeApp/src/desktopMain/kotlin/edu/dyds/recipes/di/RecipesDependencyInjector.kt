package edu.dyds.recipes.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.recipes.data.external.RecipeExternalSourceBroker
import edu.dyds.recipes.data.external.openfoodfacts.OpenFoodFactsRecipesExternalSource
import edu.dyds.recipes.data.external.themealdb.TheMealDBRecipesExternalSource
import edu.dyds.recipes.data.local.RecipesLocalDataSourceImpl
import edu.dyds.recipes.data.repository.RecipesRepositoryImpl
import edu.dyds.recipes.domain.qualifier.RecipeQualifier
import edu.dyds.recipes.domain.usecase.GetPopularRecipesUseCaseImpl
import edu.dyds.recipes.domain.usecase.GetRecipeDetailsUseCaseImpl
import edu.dyds.recipes.presentation.detail.DetailViewModel
import edu.dyds.recipes.presentation.home.HomeViewModel

object RecipesDependencyInjector {

    private val themealdbDataSource = TheMealDBRecipesExternalSource()
    private val openFoodFactsDataSource = OpenFoodFactsRecipesExternalSource()

    private val recipeExternalSourceBroker = RecipeExternalSourceBroker(
        openFoodFactsRecipeSource = openFoodFactsDataSource,
        themealdbRecipeSource = themealdbDataSource
    )

    private val localDataSource = RecipesLocalDataSourceImpl()
    private val recipeQualifier = RecipeQualifier()

    private val recipesRepository = RecipesRepositoryImpl(
        recipeDetailExternalSource = recipeExternalSourceBroker,
        popularRecipesExternalSource = themealdbDataSource,
        localDataSource = localDataSource
    )

    private val getRecipeDetailsUseCase = GetRecipeDetailsUseCaseImpl(recipesRepository)
    private val getPopularRecipesUseCase = GetPopularRecipesUseCaseImpl(recipesRepository, recipeQualifier)

    @Composable
    fun getDetailViewModel(): DetailViewModel {
        return viewModel { DetailViewModel(getRecipeDetailsUseCase) }
    }

    @Composable
    fun getHomeViewModel(): HomeViewModel {
        return viewModel { HomeViewModel(getPopularRecipesUseCase) }
    }
}

