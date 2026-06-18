@file:Suppress("FunctionName")

package edu.dyds.countries.presentation.compare

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.presentation.utils.FlagImage

private val ScreenContentPadding = 16.dp
private val ColumnLayoutSpacing = 16.dp
private val InputToContentSpacing = 16.dp
private val SectionVerticalSpacing = 16.dp

private val MaxFlagHeight = 140.dp
private val FlagToTitleSpacing = 8.dp
private val SectionTitleToContentSpacing = 4.dp
private val LabeledValueVerticalPadding = 4.dp

private const val EqualColumnWeight = 1f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(
    viewModel: CompareViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compare Countries") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(ScreenContentPadding),
            horizontalArrangement = Arrangement.spacedBy(ColumnLayoutSpacing)
        ) {
            CompareColumn(
                query = uiState.firstQuery,
                onQueryChange = viewModel::onFirstQueryChange,
                onSearch = viewModel::searchFirst,
                country = uiState.firstCountry,
                isLoading = uiState.isFirstLoading,
                modifier = Modifier.weight(EqualColumnWeight)
            )
            CompareColumn(
                query = uiState.secondQuery,
                onQueryChange = viewModel::onSecondQueryChange,
                onSearch = viewModel::searchSecond,
                country = uiState.secondCountry,
                isLoading = uiState.isSecondLoading,
                modifier = Modifier.weight(EqualColumnWeight)
            )
        }
    }
}

@Composable
private fun CompareColumn(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    country: Country?,
    isLoading: Boolean,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
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

        Spacer(modifier = Modifier.height(InputToContentSpacing))

        when {
            isLoading -> Box(Modifier.fillMaxWidth(), Alignment.Center) { CircularProgressIndicator() }
            country == null -> Text(
                "Search a country to compare",
                style = MaterialTheme.typography.bodyMedium
            )
            else -> CountryComparison(country)
        }
    }
}

@Composable
private fun CountryComparison(country: Country) {
    FlagImage(
        url = country.flagPng,
        contentDescription = country.name,
        modifier = Modifier.fillMaxWidth().heightIn(max = MaxFlagHeight),
        contentScale = ContentScale.Fit
    )

    Spacer(modifier = Modifier.height(FlagToTitleSpacing))

    Text(country.name, style = MaterialTheme.typography.titleLarge)
    Text(country.officialName, style = MaterialTheme.typography.bodySmall)

    Section("Geography") {
        LabeledValue("Capital", country.capital)
        LabeledValue("Region", country.region)
        LabeledValue("Subregion", country.subregion)
        LabeledValue("Area", country.areaKm2?.let { "${formatNumber(it)} km²" }.orEmpty())
    }

    Section("Economy") {
        LabeledValue("Population", formatNumber(country.population.toDouble()))
        LabeledValue("Currencies", country.currencies.joinToString("\n") { "${it.name} (${it.code}) ${it.symbol}".trim() })
    }

    Section("Languages") {
        LabeledValue("Languages", country.languages.joinToString(", "))
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Spacer(modifier = Modifier.height(SectionVerticalSpacing))
    Text(title, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(SectionTitleToContentSpacing))
    content()
}

@Composable
private fun LabeledValue(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = LabeledValueVerticalPadding)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(value.ifBlank { "—" }, style = MaterialTheme.typography.bodyMedium)
    }
}

private fun formatNumber(value: Double): String {
    val whole = value.toLong()
    return "%,d".format(whole)
}