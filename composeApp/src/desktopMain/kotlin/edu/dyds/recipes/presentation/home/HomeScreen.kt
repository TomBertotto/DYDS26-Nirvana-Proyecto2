@file:Suppress("FunctionName")

package edu.dyds.recipes.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.dyds.recipes.domain.entity.QualifiedRecipe
import edu.dyds.recipes.presentation.utils.LoadingIndicator
import edu.dyds.recipes.presentation.utils.NoResults
import edu.dyds.recipes.presentation.utils.RecipeImage
import kotlinx.coroutines.launch

private enum class SearchMode { NAME, CATEGORY }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onRecipeClick: (QualifiedRecipe) -> Unit,
    onOpenWeeklyPlan: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipes") },
                actions = {
                    IconButton(onClick = onOpenWeeklyPlan) {
                        Icon(Icons.Default.DateRange, contentDescription = "Weekly plan")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            SearchSection(
                onSearchByName = viewModel::searchByName,
                onSearchByCategory = viewModel::searchByCategory,
                onClear = viewModel::loadDefaultRecipes
            )
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when {
                    uiState.isLoading -> LoadingIndicator()
                    uiState.recipes.isEmpty() -> NoResults()
                    else -> RecipeList(
                        recipes = uiState.recipes,
                        onRecipeClick = onRecipeClick,
                        onAddToPlan = { qualifiedRecipe ->
                            viewModel.addToWeeklyPlan(qualifiedRecipe.recipe)
                            scope.launch { snackbarHostState.showSnackbar("Added to weekly plan") }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchSection(
    onSearchByName: (String) -> Unit,
    onSearchByCategory: (String) -> Unit,
    onClear: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf(SearchMode.NAME) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search recipes") },
            singleLine = true,
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(
                        onClick = {
                            query = ""
                            onClear()
                        }
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = mode == SearchMode.NAME,
                onClick = { mode = SearchMode.NAME },
                label = { Text("By name") }
            )
            FilterChip(
                selected = mode == SearchMode.CATEGORY,
                onClick = { mode = SearchMode.CATEGORY },
                label = { Text("By category") }
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    val trimmed = query.trim()
                    when {
                        trimmed.isBlank() -> onClear()
                        mode == SearchMode.NAME -> onSearchByName(trimmed)
                        else -> onSearchByCategory(trimmed)
                    }
                }
            ) {
                Text("Search")
            }
        }
    }
}

@Composable
private fun RecipeList(
    recipes: List<QualifiedRecipe>,
    onRecipeClick: (QualifiedRecipe) -> Unit,
    onAddToPlan: (QualifiedRecipe) -> Unit,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(recipes) { recipe ->
            RecipeCard(recipe, onRecipeClick, onAddToPlan)
        }
    }
}

@Composable
private fun RecipeCard(
    qualifiedRecipe: QualifiedRecipe,
    onRecipeClick: (QualifiedRecipe) -> Unit,
    onAddToPlan: (QualifiedRecipe) -> Unit
) {
    val recipe = qualifiedRecipe.recipe
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onRecipeClick(qualifiedRecipe) }
    ) {
        Column {
            RecipeImage(
                imageUrl = recipe.image,
                contentDescription = recipe.name,
                modifier = Modifier.fillMaxWidth()
            )
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
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onAddToPlan(qualifiedRecipe) }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add to plan")
                    }
                }
            }
        }
    }
}
