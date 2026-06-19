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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import edu.dyds.countries.domain.entity.Country
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
        containerColor = Color(0xFFF5F5F5),
        bottomBar = {
            var selectedIndex by remember { mutableStateOf(0) }
                BottomNavigationBar(
                    selectedIndex = selectedIndex,
                    onIndexChanged = { newIndex ->
                        selectedIndex = newIndex
                    },
                    onCompareClick = onCompareClick
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
                onCriteriaSelected = { newCriteria ->
                    viewModel.onCriteriaChange(newCriteria)
                    if (newCriteria == SearchCriteria.ALL) {
                        viewModel.onQueryChange("")
                    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CriteriaFilterRow(
    selectedCriteria: SearchCriteria,
    onCriteriaSelected: (SearchCriteria) -> Unit
) {
    val regions = listOf("All", "Name", "Region", "Language")
    var selectedRegion by remember { mutableStateOf(regions.first()) }
    LaunchedEffect(selectedCriteria) {
        selectedRegion = when (selectedCriteria) {
            SearchCriteria.ALL -> "All"
            SearchCriteria.NAME -> "Name"
            SearchCriteria.REGION -> "Region"
            SearchCriteria.LANGUAGE -> "Language"
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = FilterRowHorizontalPadding, vertical = FilterRowVerticalPadding),
        horizontalArrangement = Arrangement.spacedBy(FilterRowSpacing)
    ) {
        regions.forEach { region ->
            val isSelected = selectedRegion == region
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) Color(0xFF1565C0) else Color.White,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                modifier = Modifier
                    .defaultMinSize(minHeight = 36.dp)
                    .padding(end = 0.dp)
                    .clickable {
                        selectedRegion = region
                        val criteria = when (region) {
                            "All" -> SearchCriteria.ALL
                            "Name" -> SearchCriteria.NAME
                            "Region" -> SearchCriteria.REGION
                            "Language" -> SearchCriteria.LANGUAGE
                            else -> SearchCriteria.ALL
                        }
                        onCriteriaSelected(criteria)
                    }
            ) {
                Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = region,
                        color = if (isSelected) Color.White else Color.DarkGray
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
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
                            Icon(Icons.Filled.Place, contentDescription = "Location", tint = Color(0xFF1565C0), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = country.capital, color = Color.Gray, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFE3F2FD)) {
                            Text(text = country.region, color = Color(0xFF1565C0), fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Go", tint = Color.LightGray)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomNavigationBar(
    selectedIndex: Int,
    onIndexChanged: (Int) -> Unit,
    onCompareClick: () -> Unit
) {
    val selectedColor = Color(0xFF1565C0)
    val navigationItems = listOf(
        BottomNavItem("Explore", Icons.Filled.Search, 0),
        BottomNavItem("Favorites", Icons.Filled.Favorite, 1),
        BottomNavItem("Versus", Icons.Filled.Star, 2),
        BottomNavItem("Profile", Icons.Filled.Person, 3)
    )

    NavigationBar(containerColor = Color.White) {
        navigationItems.forEach { item ->
            NavigationBarItem(
                selected = selectedIndex == item.index,
                onClick = {
                    onIndexChanged(item.index)
                    if (item.index == 2) onCompareClick()
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedColor,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = selectedColor,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}

private data class BottomNavItem(val label: String, val icon: ImageVector, val index: Int)
