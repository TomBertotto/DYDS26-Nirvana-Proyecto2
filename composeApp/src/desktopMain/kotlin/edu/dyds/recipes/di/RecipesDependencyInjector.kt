package edu.dyds.recipes.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.recipes.data.external.RecipeExternalSourceBroker
import edu.dyds.recipes.data.external.openfoodfacts.OpenFoodFactsRecipesExternalSource
import edu.dyds.recipes.data.external.proxy.OpenFoodFactsRecipeProxy
import edu.dyds.recipes.data.external.proxy.TheMealDBRecipeProxy
import edu.dyds.recipes.data.external.themealdb.TheMealDBRecipesExternalSource
import edu.dyds.recipes.data.local.RecipesLocalDataSourceImpl
import edu.dyds.recipes.data.local.WeeklyPlanFileLocalDataSource
import edu.dyds.recipes.data.repository.RecipesRepositoryImpl
import edu.dyds.recipes.data.repository.WeeklyPlanRepositoryImpl
import edu.dyds.recipes.domain.qualifier.RecipeQualifier
import edu.dyds.recipes.domain.usecase.AddRecipeToWeeklyPlanUseCaseImpl
import edu.dyds.recipes.domain.usecase.GetDefaultRecipesUseCaseImpl
import edu.dyds.recipes.domain.usecase.GetRecipeDetailsUseCaseImpl
import edu.dyds.recipes.domain.usecase.GetWeeklyPlanUseCaseImpl
import edu.dyds.recipes.domain.usecase.SaveWeeklyPlanUseCaseImpl
import edu.dyds.recipes.domain.usecase.SearchRecipesByCategoryUseCaseImpl
import edu.dyds.recipes.domain.usecase.SearchRecipesByNameUseCaseImpl
import java.io.File
import edu.dyds.recipes.presentation.detail.DetailViewModel
import edu.dyds.recipes.presentation.home.HomeViewModel
import edu.dyds.recipes.presentation.plan.WeeklyPlanViewModel
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
        recipesSearchExternalSource = themealdbDataSource,
        localDataSource = localDataSource
    )

    private val getRecipeDetailsUseCase = GetRecipeDetailsUseCaseImpl(recipesRepository)
    private val getDefaultRecipesUseCase = GetDefaultRecipesUseCaseImpl(recipesRepository, recipeQualifier)
    private val searchRecipesByNameUseCase = SearchRecipesByNameUseCaseImpl(recipesRepository, recipeQualifier)
    private val searchRecipesByCategoryUseCase = SearchRecipesByCategoryUseCaseImpl(recipesRepository, recipeQualifier)

    private val weeklyPlanJson = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    private val weeklyPlanFile = File(System.getProperty("user.dir"), ".recipes/weekly_plan.json")
    private val weeklyPlanLocalDataSource = WeeklyPlanFileLocalDataSource(weeklyPlanFile, weeklyPlanJson)
    private val weeklyPlanRepository = WeeklyPlanRepositoryImpl(weeklyPlanLocalDataSource)

    private val getWeeklyPlanUseCase = GetWeeklyPlanUseCaseImpl(weeklyPlanRepository)
    private val saveWeeklyPlanUseCase = SaveWeeklyPlanUseCaseImpl(weeklyPlanRepository)
    private val addRecipeToWeeklyPlanUseCase = AddRecipeToWeeklyPlanUseCaseImpl(weeklyPlanRepository)

    @Composable
    fun getDetailViewModel(): DetailViewModel {
        return viewModel { DetailViewModel(getRecipeDetailsUseCase, addRecipeToWeeklyPlanUseCase) }
    }

    @Composable
    fun getHomeViewModel(): HomeViewModel {
        return viewModel {
            HomeViewModel(
                getDefaultRecipesUseCase = getDefaultRecipesUseCase,
                searchRecipesByNameUseCase = searchRecipesByNameUseCase,
                searchRecipesByCategoryUseCase = searchRecipesByCategoryUseCase,
                addRecipeToWeeklyPlanUseCase = addRecipeToWeeklyPlanUseCase
            )
        }
    }

    @Composable
    fun getWeeklyPlanViewModel(): WeeklyPlanViewModel {
        return viewModel { WeeklyPlanViewModel(getWeeklyPlanUseCase) }
    }
}

