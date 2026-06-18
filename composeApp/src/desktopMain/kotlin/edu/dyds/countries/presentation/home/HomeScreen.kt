@file:Suppress("FunctionName")

package edu.dyds.countries.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.presentation.utils.FlagImage
import edu.dyds.countries.presentation.utils.LoadingIndicator
private val SearchBarPadding = 16.dp
private val FilterRowHorizontalPadding = 16.dp
private val FilterRowVerticalPadding = 8.dp
private val FilterRowSpacing = 8.dp

private val GridPadding = 8.dp
private val GridHorizontalSpacing = 8.dp
private val GridVerticalSpacing = 8.dp
private val GridCellMinSize = 140.dp

private val CardPadding = 8.dp
private val CardContentSpacing = 4.dp
private const val FlagAspectRatio = 1.5f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCountryClick: (Country) -> Unit,
    onCompareClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadInitialCountries()
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Countries") },
                actions = {
                    TextButton(onClick = onCompareClick) {
                        Text("Compare")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            SearchBar(
                query = uiState.query,
                onQueryChange = viewModel::onQueryChange,
                onSearch = viewModel::search
            )

            CriteriaFilterRow(
                selectedCriteria = uiState.selectedCriteria,
                onCriteriaSelected = { newCriteria ->
                    viewModel.onCriteriaChange(newCriteria)
                    viewModel.search()
                }
            )

            when {
                uiState.isLoading -> LoadingIndicator()
                uiState.countries.isEmpty() -> EmptyState(uiState.query)
                else -> GridCountryList(uiState.countries, onCountryClick)
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
        modifier = Modifier.fillMaxWidth().padding(SearchBarPadding),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CriteriaFilterRow(
    selectedCriteria: SearchCriteria,
    onCriteriaSelected: (SearchCriteria) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = FilterRowHorizontalPadding, vertical = FilterRowVerticalPadding),
        horizontalArrangement = Arrangement.spacedBy(FilterRowSpacing)
    ) {
        SearchCriteria.entries.forEach { criteria ->
            FilterChip(
                selected = selectedCriteria == criteria,
                onClick = { onCriteriaSelected(criteria) },
                label = { Text(criteria.displayName) }
            )
        }
    }
}

@Composable
private fun GridCountryList(
    countries: List<Country>,
    onCountryClick: (Country) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = GridCellMinSize),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(GridPadding),
        horizontalArrangement = Arrangement.spacedBy(GridHorizontalSpacing),
        verticalArrangement = Arrangement.spacedBy(GridVerticalSpacing)
    ) {
        items(countries) { country ->
            GridCountryCard(country, onCountryClick)
        }
    }
}

@Composable
private fun GridCountryCard(
    country: Country,
    onCountryClick: (Country) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCountryClick(country) }
    ) {
        Column(
            modifier = Modifier.padding(CardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FlagImage(
                url = country.flagPng,
                contentDescription = country.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(FlagAspectRatio)
            )
            Spacer(modifier = Modifier.height(CardContentSpacing))
            Text(
                text = country.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}