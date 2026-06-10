@file:Suppress("FunctionName")

package edu.dyds.recipes.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.dyds.recipes.di.RecipesDependencyInjector
import edu.dyds.recipes.presentation.detail.DetailScreen
import edu.dyds.recipes.presentation.detail.DetailViewModel
import edu.dyds.recipes.presentation.home.HomeScreen
import edu.dyds.recipes.presentation.home.HomeViewModel

private const val HOME = "home"

private const val DETAIL = "detail"

private const val RECIPE_ID = "recipeId"

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val detailViewModel = RecipesDependencyInjector.getDetailViewModel()
    val homeViewModel = RecipesDependencyInjector.getHomeViewModel()

    NavHost(navController = navController, startDestination = HOME) {
        homeDestination(navController, homeViewModel)
        detailDestination(navController, detailViewModel)
    }
}

private fun NavGraphBuilder.homeDestination(navController: NavHostController, homeViewModel: HomeViewModel) {
    composable(HOME) {
        HomeScreen(
            viewModel = homeViewModel,
            onRecipeClick = {
                navController.navigate("$DETAIL/${it.recipe.id}")
            }
        )
    }
}

private fun NavGraphBuilder.detailDestination(navController: NavHostController, detailViewModel: DetailViewModel) {
    composable(
        route = "$DETAIL/{$RECIPE_ID}",
        arguments = listOf(navArgument(RECIPE_ID) { type = NavType.StringType })
    ) { backstackEntry ->
        val recipeId = backstackEntry.arguments?.getString(RECIPE_ID)

        recipeId?.let {
            DetailScreen(detailViewModel, it, onBack = { navController.popBackStack() })
        }
    }
}

