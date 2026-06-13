@file:Suppress("FunctionName")

package edu.dyds.recipes.presentation.plan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.dyds.recipes.domain.entity.Recipe
import edu.dyds.recipes.presentation.utils.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyPlanScreen(
    viewModel: WeeklyPlanViewModel,
    onRecipeClick: (Recipe) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWeeklyPlan()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weekly Plan") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when {
                uiState.isLoading -> LoadingIndicator()
                uiState.recipes.isEmpty() -> Unit
                else -> WeeklyPlanList(uiState.recipes, onRecipeClick)
            }
        }
    }
}

@Composable
private fun WeeklyPlanList(
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(recipes) { recipe ->
            WeeklyPlanCard(recipe, onRecipeClick)
        }
    }
}

@Composable
private fun WeeklyPlanCard(
    recipe: Recipe,
    onRecipeClick: (Recipe) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onRecipeClick(recipe) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(recipe.name, style = MaterialTheme.typography.headlineSmall)
            Text(recipe.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
