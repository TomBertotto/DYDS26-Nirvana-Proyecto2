@file:Suppress("FunctionName")

package edu.dyds.countries.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.SearchCriteria
import edu.dyds.countries.presentation.components.AppColors
import edu.dyds.countries.presentation.components.COMPARE_NAV_INDEX
import edu.dyds.countries.presentation.components.CountriesBottomNavigationBar
import edu.dyds.countries.presentation.utils.FlagImage
import edu.dyds.countries.presentation.utils.LoadingIndicator

private val SearchBarPadding = 16.dp
private val FilterRowHorizontalPadding = 16.dp
private val FilterRowVerticalPadding = 8.dp
private val FilterRowSpacing = 8.dp

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
        containerColor = AppColors.ScreenBackground,
        bottomBar = {
            var selectedIndex by remember { mutableStateOf(0) }
            CountriesBottomNavigationBar(
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    selectedIndex = index
                    if (index == COMPARE_NAV_INDEX) onCompareClick()
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Text(
                text = "Countries",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
            )
            SearchBar(
                query = uiState.query,
                onQueryChange = viewModel::onQueryChange,
                onSearch = viewModel::search
            )

            CriteriaFilterRow(
                selectedCriteria = uiState.selectedCriteria,
                onCriteriaSelected = { criteria ->
                    viewModel.onCriteriaChange(criteria)
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
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(SearchBarPadding),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(text = "Search countries...", color = Color.Gray)
                }
                BasicTextField(
                    value = query,
                    onValueChange = { newQuery ->
                        onQueryChange(newQuery)
                        onSearch()
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
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
            val isSelected = selectedCriteria == criteria
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) AppColors.PrimaryBlue else Color.White,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                modifier = Modifier
                    .defaultMinSize(minHeight = 36.dp)
                    .clickable { onCriteriaSelected(criteria) }
            ) {
                Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = criteria.label,
                        color = if (isSelected) Color.White else Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
private fun GridCountryList(
    countries: List<Country>,
    onCountryClick: (Country) -> Unit
) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(countries) { country ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCountryClick(country) }
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FlagImage(
                        url = country.flagPng,
                        contentDescription = country.name,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = country.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Place, contentDescription = "Location", tint = AppColors.PrimaryBlue, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = country.capital, color = Color.Gray, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(shape = RoundedCornerShape(8.dp), color = AppColors.LightBlueChip) {
                            Text(text = country.region, color = AppColors.PrimaryBlue, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Go", tint = Color.LightGray)
                }
            }
        }
    }
}
