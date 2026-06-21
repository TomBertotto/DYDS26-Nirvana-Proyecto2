@file:Suppress("FunctionName")

package edu.dyds.countries.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.Weather
import edu.dyds.countries.presentation.components.AppColors
import edu.dyds.countries.presentation.utils.FlagImage
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
        containerColor = AppColors.ScreenBackground,
        topBar = {
            TopAppBar(
                title = { Text("Country Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = DetailColors.PrimaryBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> LoadingIndicator()
            uiState.country == null -> NoResults()
            else -> CountryDetailsContent(
                country = uiState.country!!,
                weather = uiState.weather,
                isWeatherLoading = uiState.isWeatherLoading,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}


private object DetailColors {
    val PrimaryBlue = AppColors.PrimaryBlue
}

private object DetailDimens {
    val CardPadding = 16.dp
    val CornerRadius = 16.dp
}

@Composable
private fun CountryDetailsContent(
    country: Country,
    weather: Weather?,
    isWeatherLoading: Boolean,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item { HeroImage(country) }

        item {
            CountryHeaderCard(country)
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item { QuickStatsCard(country) }

        if (isWeatherLoading || weather != null) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { WeatherCard(capital = country.capital, weather = weather, isLoading = isWeatherLoading) }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item { GeographicInfoCard(country) }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item { LanguagesCultureCard(country) }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun HeroImage(country: Country) {
    Box {
        FlagImage(
            url = country.flagPng,
            contentDescription = country.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun CountryHeaderCard(country: Country) {
    Card(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-24).dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
            Text(
                country.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                country.officialName,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun QuickStatsCard(country: Country) {
    Card(
        shape = RoundedCornerShape(DetailDimens.CornerRadius),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(DetailDimens.CardPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatItem(
                    icon = Icons.Default.LocationOn,
                    label = "Capital",
                    value = country.capital,
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    icon = Icons.Default.People,
                    label = "Population",
                    value = formatPopulation(country.population),
                    modifier = Modifier.weight(1f)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.LightGray)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatItem(
                    icon = Icons.Default.Map,
                    label = "Area",
                    value = if (country.areaKm2 != null) "${String.format("%.0f", country.areaKm2)} km²" else "—",
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    icon = Icons.Default.Language,
                    label = "Region",
                    value = country.region,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = DetailColors.PrimaryBlue,
            modifier = Modifier.size(20.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                value.ifBlank { "—" },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun WeatherCard(capital: String, weather: Weather?, isLoading: Boolean) {
    Card(
        shape = RoundedCornerShape(DetailDimens.CornerRadius),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(DetailDimens.CardPadding)) {
            Text(
                if (capital.isBlank()) "Capital Weather" else "$capital Weather",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            when {
                isLoading -> Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(28.dp), strokeWidth = 2.dp)
                }
                weather != null -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "${String.format("%.1f", weather.temperature)}°C",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = DetailColors.PrimaryBlue
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            weather.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        WeatherMetric("Feels like", "${String.format("%.1f", weather.apparentTemperature)}°C", Modifier.weight(1f))
                        WeatherMetric("Humidity", "${weather.humidity}%", Modifier.weight(1f))
                        WeatherMetric("Wind", "${String.format("%.1f", weather.windSpeed)} km/h", Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun GeographicInfoCard(country: Country) {
    Card(
        shape = RoundedCornerShape(DetailDimens.CornerRadius),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(DetailDimens.CardPadding)) {
            Text(
                "Geographic Information",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            IconDetailRow(icon = Icons.Default.Map, label = "Subregion", value = country.subregion)
        }
    }
}

@Composable
private fun LanguagesCultureCard(country: Country) {
    Card(
        shape = RoundedCornerShape(DetailDimens.CornerRadius),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(DetailDimens.CardPadding)) {
            Text(
                "Languages & Culture",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            val languagesList = country.languages.joinToString(", ").ifBlank { "—" }
            IconDetailRow(icon = Icons.Default.Chat, label = "Official Languages", value = languagesList)

            val currenciesList = country.currencies.joinToString(", ") { it.code }.ifBlank { "—" }
            IconDetailRow(icon = Icons.Default.AttachMoney, label = "Currency", value = currenciesList)
        }
    }
}

@Composable
private fun IconDetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = DetailColors.PrimaryBlue,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Text(value.ifBlank { "—" }, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}

private fun formatPopulation(population: Long): String {
    return when {
        population >= 1_000_000 -> String.format("%.1fM", population / 1_000_000.0)
        population >= 1_000 -> String.format("%.1fK", population / 1_000.0)
        else -> population.toString()
    }
}




