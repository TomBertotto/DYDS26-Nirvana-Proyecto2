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
import edu.dyds.countries.domain.entity.Country
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
            else -> CountryDetails(uiState.country!!, Modifier.padding(paddingValues))
        }
    }
}

@Composable
private fun CountryDetails(country: Country, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        FlagImage(
            url = country.flagPng,
            contentDescription = country.name,
            modifier = Modifier.fillMaxWidth().height(180.dp)
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
