@file:Suppress("FunctionName")

package edu.dyds.countries.presentation.compare

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.presentation.utils.FlagImage

private val CardPadding = 16.dp
private val CardSpacing = 12.dp
private val PrimaryBlue = Color(0xFF1565C0)
private val LightGray = Color(0xFFF5F5F5)
private val DividerGray = Color(0xFFE0E0E0)
private val TextGray = Color(0xFF9E9E9E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(
    viewModel: CompareViewModel,
    onBack: () -> Unit,
    onCompareClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFirstSearchDialog by remember { mutableStateOf(false) }
    var showSecondSearchDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = LightGray,
        bottomBar = {
            CompareBottomNavigationBar(
                onNavigationItemClick = { index ->
                    if (index != 2) onBack()
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(CardPadding),
            verticalArrangement = Arrangement.spacedBy(CardSpacing)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = PrimaryBlue)
                    }
                    Text(
                        text = "Compare countries",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                CountriesCard(
                    country1 = uiState.firstCountry,
                    country2 = uiState.secondCountry,
                    isLoading1 = uiState.isFirstLoading,
                    isLoading2 = uiState.isSecondLoading,
                    error1 = uiState.firstError,
                    error2 = uiState.secondError,
                    onCountry1Click = { showFirstSearchDialog = true },
                    onCountry2Click = { showSecondSearchDialog = true }
                )
            }

            if (uiState.firstCountry != null || uiState.secondCountry != null) {
                item {
                    ComparisonStatsCard(
                        country1 = uiState.firstCountry,
                        country2 = uiState.secondCountry
                    )
                }
            }
        }
    }

    if (showFirstSearchDialog) {
        SearchDialog(
            query = uiState.firstQuery,
            onQueryChange = viewModel::onFirstQueryChange,
            onSearch = viewModel::searchFirst,
            onDismiss = { showFirstSearchDialog = false }
        )
    }

    if (showSecondSearchDialog) {
        SearchDialog(
            query = uiState.secondQuery,
            onQueryChange = viewModel::onSecondQueryChange,
            onSearch = viewModel::searchSecond,
            onDismiss = { showSecondSearchDialog = false }
        )
    }
}

@Composable
private fun CountriesCard(
    country1: Country?,
    country2: Country?,
    isLoading1: Boolean,
    isLoading2: Boolean,
    error1: String?,
    error2: String?,
    onCountry1Click: () -> Unit,
    onCountry2Click: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CountryRow(
                country = country1,
                isLoading = isLoading1,
                error = error1,
                onClick = onCountry1Click
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = DividerGray)
                Text(
                    text = "VS",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = DividerGray)
            }

            CountryRow(
                country = country2,
                isLoading = isLoading2,
                error = error2,
                onClick = onCountry2Click
            )
        }
    }
}

@Composable
private fun CountryRow(
    country: Country?,
    isLoading: Boolean,
    error: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isLoading && error == null) { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                strokeWidth = 2.dp
            )
        } else if (error != null) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = Color(0xFFFFEBEE)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("!", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
        } else if (country != null) {
            FlagImage(
                url = country.flagPng,
                contentDescription = country.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = LightGray
            ) {}
        }

        Column(modifier = Modifier.weight(1f)) {
            when {
                error != null -> Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Red
                )
                country != null -> {
                    Text(
                        text = country.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = PrimaryBlue
                        )
                        Text(
                            text = country.capital,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
                else -> Text(
                    text = "Select a country",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }
        }

        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color(0xFFCCCCCC),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ComparisonStatsCard(
    country1: Country?,
    country2: Country?
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val capital1 = country1?.capital.orEmpty()
            val capital2 = country2?.capital.orEmpty()
            CompareStatRow("Capital", capital1, capital2, Icons.Default.LocationOn)

            val area1 = country1?.areaKm2?.let { formatNumber(it) }.orEmpty()
            val area2 = country2?.areaKm2?.let { formatNumber(it) }.orEmpty()
            val areaHighlight1 = (country1?.areaKm2 ?: 0.0) > (country2?.areaKm2 ?: 0.0) && country1?.areaKm2 != null
            val areaHighlight2 = (country2?.areaKm2 ?: 0.0) > (country1?.areaKm2 ?: 0.0) && country2?.areaKm2 != null
            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF0F0F0))
            CompareStatRow("Area", "$area1 km²", "$area2 km²", Icons.Default.Map, areaHighlight1, areaHighlight2)

            val pop1 = country1?.population?.toDouble() ?: 0.0
            val pop2 = country2?.population?.toDouble() ?: 0.0
            val pop1Formatted = if (pop1 > 0) formatPopulation(pop1) else ""
            val pop2Formatted = if (pop2 > 0) formatPopulation(pop2) else ""
            val popHighlight1 = pop1 > pop2 && country1 != null
            val popHighlight2 = pop2 > pop1 && country2 != null
            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF0F0F0))
            CompareStatRow("Population", pop1Formatted, pop2Formatted, Icons.Default.People, popHighlight1, popHighlight2)

            val region1 = country1?.region.orEmpty()
            val region2 = country2?.region.orEmpty()
            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF0F0F0))
            CompareStatRow("Region", region1, region2, Icons.Default.Language)

            val subregion1 = country1?.subregion.orEmpty()
            val subregion2 = country2?.subregion.orEmpty()
            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF0F0F0))
            CompareStatRow("Subregion", subregion1, subregion2, Icons.Default.Map)

            val langs1 = country1?.languages?.joinToString(", ").orEmpty()
            val langs2 = country2?.languages?.joinToString(", ").orEmpty()
            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF0F0F0))
            CompareStatRow("Languages", langs1, langs2, Icons.Default.Chat)
        }
    }
}

@Composable
private fun CompareStatRow(
    label: String,
    value1: String,
    value2: String,
    icon: ImageVector? = null,
    highlight1: Boolean = false,
    highlight2: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (icon != null) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Spacer(Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            StatValueBox(value1.ifBlank { "—" }, Modifier.weight(1f), highlight1)
            Spacer(Modifier.width(8.dp))
            StatValueBox(value2.ifBlank { "—" }, Modifier.weight(1f), highlight2)
        }
    }
}

@Composable
private fun StatValueBox(value: String, modifier: Modifier, highlighted: Boolean = false) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = if (highlighted) Color(0xFFE3F2FD) else LightGray
    ) {
        Text(
            text = value,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (highlighted) FontWeight.Bold else FontWeight.SemiBold,
            color = if (highlighted) PrimaryBlue else Color(0xFF212121),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SearchDialog(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Search Country") },
        text = {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Country name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() })
            )
        },
        confirmButton = {
            Button(onClick = {
                onSearch()
                onDismiss()
            }) {
                Text("Search")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompareBottomNavigationBar(
    onNavigationItemClick: (Int) -> Unit
) {
    val selectedColor = PrimaryBlue
    val navigationItems = listOf(
        BottomNavItem("Explore", Icons.Filled.Search, 0),
        BottomNavItem("Favorites", Icons.Filled.Favorite, 1),
        BottomNavItem("Versus", Icons.Filled.Star, 2),
        BottomNavItem("Profile", Icons.Filled.Person, 3)
    )

    NavigationBar(containerColor = Color.White) {
        navigationItems.forEach { item ->
            NavigationBarItem(
                selected = item.index == 2,
                onClick = { onNavigationItemClick(item.index) },
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

private fun formatPopulation(value: Double): String = when {
    value >= 1_000_000 -> String.format("%.1fM", value / 1_000_000)
    value >= 1_000 -> String.format("%.1fK", value / 1_000)
    else -> value.toLong().toString()
}

private fun formatNumber(value: Double): String {
    val whole = value.toLong()
    return "%,d".format(whole)
}