@file:Suppress("FunctionName")

package edu.dyds.countries.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.dyds.countries.domain.entity.QualifiedCountry
import edu.dyds.countries.presentation.utils.LoadingIndicator
import edu.dyds.countries.presentation.utils.NoResults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCountryClick: (QualifiedCountry) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Countries") })
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> LoadingIndicator()
            uiState.countries.isEmpty() -> NoResults()
            else -> CountryList(uiState.countries, onCountryClick, Modifier.padding(paddingValues))
        }
    }
}

@Composable
private fun CountryList(
    countries: List<QualifiedCountry>,
    onCountryClick: (QualifiedCountry) -> Unit,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(countries) { country ->
            CountryCard(country, onCountryClick)
        }
    }
}

@Composable
private fun CountryCard(
    qualifiedCountry: QualifiedCountry,
    onCountryClick: (QualifiedCountry) -> Unit
) {
    val country = qualifiedCountry.country
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onCountryClick(qualifiedCountry) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(country.name, style = MaterialTheme.typography.headlineSmall)
            Text(country.description, style = MaterialTheme.typography.bodyMedium)
            Text("Rating: ${country.rating}", style = MaterialTheme.typography.labelSmall)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Servings: ${country.servings}")
                Text("Prep: ${country.prepTime}m")
                Text("Cook: ${country.cookTime}m")
            }
        }
    }
}
