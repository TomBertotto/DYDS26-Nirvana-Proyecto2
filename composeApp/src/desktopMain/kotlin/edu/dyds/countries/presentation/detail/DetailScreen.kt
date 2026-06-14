@file:Suppress("FunctionName")

package edu.dyds.countries.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.dyds.countries.presentation.utils.LoadingIndicator
import edu.dyds.countries.presentation.utils.NoResults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    countryId: String,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(countryId) {
        viewModel.getCountryDetail(countryId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.country?.name ?: "Country Details") },
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
            uiState.country == null -> NoResults()
            else -> CountryDetails(uiState.country!!, Modifier.padding(paddingValues))
        }
    }
}

@Composable
private fun CountryDetails(country: edu.dyds.countries.domain.entity.Country, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(country.name, style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Description: ${country.description}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Ingredients:", style = MaterialTheme.typography.titleMedium)
        country.ingredients.forEach { ingredient ->
            Text("• $ingredient", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Instructions:", style = MaterialTheme.typography.titleMedium)
        Text(country.instructions, modifier = Modifier.padding(start = 8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(Modifier.weight(1f)) {
                Text("Servings: ${country.servings}", style = MaterialTheme.typography.bodySmall)
                Text("Prep: ${country.prepTime}m", style = MaterialTheme.typography.bodySmall)
                Text("Cook: ${country.cookTime}m", style = MaterialTheme.typography.bodySmall)
            }
            Column(Modifier.weight(1f)) {
                Text("Calories: ${country.calories}", style = MaterialTheme.typography.bodySmall)
                Text("Rating: ${country.rating}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
