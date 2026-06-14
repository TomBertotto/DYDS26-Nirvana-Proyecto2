@file:Suppress("FunctionName")

package edu.dyds.countries.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.dyds.countries.di.CountriesDependencyInjector
import edu.dyds.countries.presentation.detail.DetailScreen
import edu.dyds.countries.presentation.detail.DetailViewModel
import edu.dyds.countries.presentation.home.HomeScreen
import edu.dyds.countries.presentation.home.HomeViewModel

private const val HOME = "home"

private const val DETAIL = "detail"

private const val COUNTRY_ID = "countryId"

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val detailViewModel = CountriesDependencyInjector.getDetailViewModel()
    val homeViewModel = CountriesDependencyInjector.getHomeViewModel()

    NavHost(navController = navController, startDestination = HOME) {
        homeDestination(navController, homeViewModel)
        detailDestination(navController, detailViewModel)
    }
}

private fun NavGraphBuilder.homeDestination(navController: NavHostController, homeViewModel: HomeViewModel) {
    composable(HOME) {
        HomeScreen(
            viewModel = homeViewModel,
            onCountryClick = {
                navController.navigate("$DETAIL/${it.id}")
            }
        )
    }
}

private fun NavGraphBuilder.detailDestination(navController: NavHostController, detailViewModel: DetailViewModel) {
    composable(
        route = "$DETAIL/{$COUNTRY_ID}",
        arguments = listOf(navArgument(COUNTRY_ID) { type = NavType.StringType })
    ) { backstackEntry ->
        val countryId = backstackEntry.arguments?.getString(COUNTRY_ID)

        countryId?.let {
            DetailScreen(detailViewModel, it, onBack = { navController.popBackStack() })
        }
    }
}
