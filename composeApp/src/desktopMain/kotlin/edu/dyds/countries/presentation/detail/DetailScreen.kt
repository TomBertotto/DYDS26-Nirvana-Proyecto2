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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.Weather
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
            else -> CountryDetails(
                country = uiState.country!!,
                weather = uiState.weather,
                isWeatherLoading = uiState.isWeatherLoading,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun CountryDetails(
    country: Country,
    weather: Weather?,
    isWeatherLoading: Boolean,
    modifier: Modifier
) {
    Row(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())
        ) {
            FlagImage(
                url = country.flagPng,
                contentDescription = country.name,
                modifier = Modifier.fillMaxWidth().heightIn(max = 240.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(country.name, style = MaterialTheme.typography.headlineLarge)
            Text(country.officialName, style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(16.dp))

            DetailRow("Capital", country.capital)
            DetailRow("Region", country.region)
            DetailRow("Subregion", country.subregion)
            DetailRow("Population", country.population.toString())
        }

        WeatherPanel(
            weather = weather,
            isLoading = isWeatherLoading,
            capital = country.capital,
            modifier = Modifier.width(220.dp)
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("$label:", style = MaterialTheme.typography.bodyMedium)
        Text(value.ifBlank { "—" }, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun WeatherPanel(
    weather: Weather?,
    isLoading: Boolean,
    capital: String,
    modifier: Modifier
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text("Weather", style = MaterialTheme.typography.titleMedium)
            if (capital.isNotBlank()) {
                Text(capital, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))

            when {
                isLoading -> CircularProgressIndicator()
                weather == null -> Text("Weather unavailable", style = MaterialTheme.typography.bodyMedium)
                else -> {
                    Text("${weather.temperature}°C", style = MaterialTheme.typography.headlineMedium)
                    Text(weather.description, style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Feels like ${weather.apparentTemperature}°C", style = MaterialTheme.typography.bodySmall)
                    Text("Humidity ${weather.humidity}%", style = MaterialTheme.typography.bodySmall)
                    Text("Wind ${weather.windSpeed} km/h", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
