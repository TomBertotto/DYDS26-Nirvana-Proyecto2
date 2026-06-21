package edu.dyds.countries.data.local

import edu.dyds.countries.domain.entity.Country

interface CountriesLocalDataSource {
    suspend fun saveCountries(countries: List<Country>)
    suspend fun getCountryById(id: String): Country?
    suspend fun getAllCountries(): List<Country>
}
