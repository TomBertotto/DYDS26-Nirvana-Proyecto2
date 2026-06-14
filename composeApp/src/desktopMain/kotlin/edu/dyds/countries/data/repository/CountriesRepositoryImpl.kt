package edu.dyds.countries.data.repository

import edu.dyds.countries.data.external.AllCountriesExternalSource
import edu.dyds.countries.data.external.CountryDetailExternalSource
import edu.dyds.countries.data.local.CountriesLocalDataSource
import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.repository.CountriesRepository

class CountriesRepositoryImpl(
    private val countryDetailExternalSource: CountryDetailExternalSource,
    private val allCountriesExternalSource: AllCountriesExternalSource,
    private val localDataSource: CountriesLocalDataSource
) : CountriesRepository {

    override suspend fun getCountryById(id: String): Country? {
        val remoteCountry = runCatching { countryDetailExternalSource.getCountryById(id) }.getOrNull()

        return remoteCountry ?: runCatching { localDataSource.getCountryById(id) }.getOrNull()
    }

    override suspend fun getAllCountries(): List<Country> {
        val localCountries = localDataSource.getAllCountries()

        if (localCountries.isNotEmpty()) {
            return localCountries
        }

        val remoteCountries = runCatching { allCountriesExternalSource.getAllCountries() }.getOrNull() ?: emptyList()

        if (remoteCountries.isNotEmpty()) {
            localDataSource.saveCountries(remoteCountries)
        }

        return remoteCountries
    }
}
