package edu.dyds.countries.domain.repository

import edu.dyds.countries.domain.entity.Country

interface CountriesRepository {
    suspend fun searchCountries(query: String, criteria: String): List<Country>
    suspend fun getCountryById(id: String): Country?
}
