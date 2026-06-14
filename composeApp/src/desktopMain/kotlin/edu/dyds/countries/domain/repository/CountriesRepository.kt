package edu.dyds.countries.domain.repository

import edu.dyds.countries.domain.entity.Country

interface CountriesRepository {
    suspend fun getCountryById(id: String): Country?
    suspend fun getAllCountries(): List<Country>
}
