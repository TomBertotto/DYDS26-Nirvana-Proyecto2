package edu.dyds.recipes.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.recipes.data.external.RecipeExternalSourceBroker
import edu.dyds.recipes.data.external.openfoodfacts.OpenFoodFactsRecipesExternalSource
import edu.dyds.recipes.data.external.proxy.OpenFoodFactsRecipeProxy
import edu.dyds.recipes.data.external.proxy.TheMealDBRecipeProxy
import edu.dyds.recipes.data.external.themealdb.TheMealDBRecipesExternalSource
import edu.dyds.recipes.data.local.RecipesLocalDataSourceImpl
import edu.dyds.recipes.data.repository.RecipesRepositoryImpl
import edu.dyds.recipes.domain.qualifier.RecipeQualifier
import edu.dyds.recipes.domain.usecase.GetPopularRecipesUseCaseImpl
import edu.dyds.recipes.domain.usecase.GetRecipeDetailsUseCaseImpl
import edu.dyds.recipes.presentation.detail.DetailViewModel
import edu.dyds.recipes.presentation.home.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.http.HttpHeaders
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object RecipesDependencyInjector {

    val openFoodFactsHttpClient = HttpClient {

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
        defaultRequest {
            header(HttpHeaders.UserAgent, "MiAppRecetas/1.0 (tu_correo@ejemplo.com)")
        }
    }
    val theMealDbHttpClient = HttpClient {

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }

        defaultRequest {
            url("https://www.themealdb.com/api/json/v1/1/")
        }
    }


    private val themealdbDataSource = TheMealDBRecipesExternalSource(theMealDbHttpClient)
    private val openFoodFactsDataSource = OpenFoodFactsRecipesExternalSource(openFoodFactsHttpClient = openFoodFactsHttpClient)
    private val openFoodFactsProxy = OpenFoodFactsRecipeProxy(openFoodFactsDataSource)

    private val theMealDBProxy = TheMealDBRecipeProxy(themealdbDataSource)
    private val recipeExternalSourceBroker = RecipeExternalSourceBroker(
        openFoodFactsRecipeSource = openFoodFactsProxy,
        themealdbRecipeSource = theMealDBProxy
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

