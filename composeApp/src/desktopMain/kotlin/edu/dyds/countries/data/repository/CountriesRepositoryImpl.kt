package edu.dyds.countries.data.repository

import edu.dyds.countries.data.external.CountriesSearchExternalSource
import edu.dyds.countries.data.external.CountryDetailExternalSource
import edu.dyds.countries.data.local.CountriesLocalDataSource
import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.repository.CountriesRepository

class CountriesRepositoryImpl(
    private val countriesSearchExternalSource: CountriesSearchExternalSource,
    private val countryDetailExternalSource: CountryDetailExternalSource,
    private val localDataSource: CountriesLocalDataSource
) : CountriesRepository {

    override suspend fun searchCountries(query: String, criteria: String): List<Country>{
        val cachedCountries = localDataSource.getAllCountries()

        if (countryInLocal(query)) {
            return filter(cachedCountries, criteria, query)
        }

        val remoteCountries = runCatching { countriesSearchExternalSource.searchCountries(query) }.getOrNull() ?: emptyList()

        if (remoteCountries.isNotEmpty()) {
            localDataSource.saveCountries(remoteCountries)
        }

        return filter(remoteCountries, criteria, query)
    }

    override suspend fun getCountryById(id: String): Country? {
        val remoteCountry = runCatching { countryDetailExternalSource.getCountryById(id) }.getOrNull()

        return remoteCountry ?: runCatching { localDataSource.getCountryById(id) }.getOrNull()
    }

    private fun filter(countries: List<Country>, criteria: String, nameCriteria : String): List<Country> {
        var contriesFilterList = emptyList<Country>()
        when (criteria) {
            "Name" -> contriesFilterList = countries.filter { it.name.contains(nameCriteria) }
            "Region" -> contriesFilterList = countries.filter { it.region.contains(nameCriteria) }
            "Language" -> contriesFilterList = countries.filter { it.languages.contains(nameCriteria) }
            "All" -> contriesFilterList = countries
        }
        return contriesFilterList
    }

    private suspend fun countryInLocal(query: String): Boolean {
        return localDataSource.getAllCountries().any { country -> country.name.contains(query, true) }
    }
}
