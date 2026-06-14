@file:Suppress("FunctionName")

package edu.dyds.countries.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.presentation.utils.FlagImage
import edu.dyds.countries.presentation.utils.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCountryClick: (Country) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Countries") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            SearchBar(
                query = uiState.query,
                onQueryChange = viewModel::onQueryChange,
                onSearch = viewModel::search
            )
            when {
                uiState.isLoading -> LoadingIndicator()
                uiState.countries.isEmpty() -> EmptyState(uiState.query)
                else -> CountryList(uiState.countries, onCountryClick)
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        label = { Text("Search a country") },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() })
    )
}

@Composable
private fun EmptyState(query: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(if (query.isBlank()) "Search for a country" else "No countries found")
    }
}

@Composable
private fun CountryList(
    countries: List<Country>,
    onCountryClick: (Country) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(countries) { country ->
            CountryCard(country, onCountryClick)
        }
    }
}

@Composable
private fun CountryCard(
    country: Country,
    onCountryClick: (Country) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onCountryClick(country) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FlagImage(
                url = country.flagPng,
                contentDescription = country.name,
                modifier = Modifier.width(80.dp).height(54.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(country.name, style = MaterialTheme.typography.titleMedium)
                Text(country.region, style = MaterialTheme.typography.bodyMedium)
                if (country.capital.isNotBlank()) {
                    Text("Capital: ${country.capital}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
