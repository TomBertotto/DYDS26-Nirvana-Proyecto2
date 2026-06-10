@file:Suppress("FunctionName")

package edu.dyds.recipes.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.dyds.recipes.domain.entity.QualifiedRecipe
import edu.dyds.recipes.presentation.utils.LoadingIndicator
import edu.dyds.recipes.presentation.utils.NoResults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onRecipeClick: (QualifiedRecipe) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Popular Recipes") })
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> LoadingIndicator()
            uiState.recipes.isEmpty() -> NoResults()
            else -> RecipeList(uiState.recipes, onRecipeClick, Modifier.padding(paddingValues))
        }
    }
}

@Composable
private fun RecipeList(
    recipes: List<QualifiedRecipe>,
    onRecipeClick: (QualifiedRecipe) -> Unit,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(recipes) { recipe ->
            RecipeCard(recipe, onRecipeClick)
        }
    }
}

@Composable
private fun RecipeCard(
    qualifiedRecipe: QualifiedRecipe,
    onRecipeClick: (QualifiedRecipe) -> Unit
) {
    val recipe = qualifiedRecipe.recipe
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onRecipeClick(qualifiedRecipe) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(recipe.name, style = MaterialTheme.typography.headlineSmall)
            Text(recipe.description, style = MaterialTheme.typography.bodyMedium)
            Text("Rating: ${recipe.rating}", style = MaterialTheme.typography.labelSmall)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Servings: ${recipe.servings}")
                Text("Prep: ${recipe.prepTime}m")
                Text("Cook: ${recipe.cookTime}m")
            }
        }
    }
}

