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

    override suspend fun searchCountries(query: String): List<Country> {
        val cachedCountries = localDataSource.getAllCountries()

        if (countryInLocal(query)) {
            return cachedCountries
        }

        val remoteCountries = runCatching { countriesSearchExternalSource.searchCountries(query) }.getOrNull() ?: emptyList()

        if (remoteCountries.isNotEmpty()) {
            localDataSource.saveCountries(remoteCountries)
        }

        return remoteCountries
    }

    override suspend fun getCountryById(id: String): Country? {
        val remoteCountry = runCatching { countryDetailExternalSource.getCountryById(id) }.getOrNull()

        return remoteCountry ?: runCatching { localDataSource.getCountryById(id) }.getOrNull()
    }

    private suspend fun countryInLocal(query: String): Boolean {
        return localDataSource.getAllCountries().any { country -> country.name.contains(query, true) }
    }
}
