@file:Suppress("FunctionName")

package edu.dyds.recipes.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.dyds.recipes.presentation.utils.LoadingIndicator
import edu.dyds.recipes.presentation.utils.NoResults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    recipeId: String,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(recipeId) {
        viewModel.getRecipeDetail(recipeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.recipe?.name ?: "Recipe Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> LoadingIndicator()
            uiState.recipe == null -> NoResults()
            else -> RecipeDetails(uiState.recipe!!, Modifier.padding(paddingValues))
        }
    }
}

@Composable
private fun RecipeDetails(recipe: edu.dyds.recipes.domain.entity.Recipe, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(recipe.name, style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Description: ${recipe.description}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Ingredients:", style = MaterialTheme.typography.titleMedium)
        recipe.ingredients.forEach { ingredient ->
            Text("• $ingredient", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Instructions:", style = MaterialTheme.typography.titleMedium)
        Text(recipe.instructions, modifier = Modifier.padding(start = 8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(Modifier.weight(1f)) {
                Text("Servings: ${recipe.servings}", style = MaterialTheme.typography.bodySmall)
                Text("Prep: ${recipe.prepTime}m", style = MaterialTheme.typography.bodySmall)
                Text("Cook: ${recipe.cookTime}m", style = MaterialTheme.typography.bodySmall)
            }
            Column(Modifier.weight(1f)) {
                Text("Calories: ${recipe.calories}", style = MaterialTheme.typography.bodySmall)
                Text("Rating: ${recipe.rating}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

