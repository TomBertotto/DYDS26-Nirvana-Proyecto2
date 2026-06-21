package edu.dyds.countries.data.local

import edu.dyds.countries.domain.entity.Country

class CountriesLocalDataSourceImpl : CountriesLocalDataSource {
    private val countryCache = mutableMapOf<String, Country>()

    override suspend fun saveCountries(countries: List<Country>) {
        countryCache.clear()
        countries.forEach { countryCache[it.id] = it }
    }

    override suspend fun getCountryById(id: String): Country? {
        return countryCache[id]
    }

    override suspend fun getAllCountries(): List<Country> {
        return countryCache.values.toList()
    }
}
