package edu.dyds.countries.data.repository

import edu.dyds.countries.data.external.CountriesListExternalSource
import edu.dyds.countries.data.external.CountryDetailExternalSource
import edu.dyds.countries.data.local.CountriesLocalDataSource
import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.repository.CountriesRepository

class CountriesRepositoryImpl(
    private val countriesListExternalSource: CountriesListExternalSource,
    private val countryDetailExternalSource: CountryDetailExternalSource,
    private val localDataSource: CountriesLocalDataSource
) : CountriesRepository {

    override suspend fun getAllCountries(): List<Country> {
        val cachedCountries = localDataSource.getAllCountries()
        if (cachedCountries.isNotEmpty()) {
            return cachedCountries
        }

        val remoteCountries = runCatching { countriesListExternalSource.getAllCountries() }.getOrNull() ?: emptyList()
        if (remoteCountries.isNotEmpty()) {
            localDataSource.saveCountries(remoteCountries)
        }

        return remoteCountries
    }

    override suspend fun getCountryById(id: String): Country? {
        val remoteCountry = runCatching { countryDetailExternalSource.getCountryById(id) }.getOrNull()

        return remoteCountry ?: runCatching { localDataSource.getCountryById(id) }.getOrNull()
    }
}
