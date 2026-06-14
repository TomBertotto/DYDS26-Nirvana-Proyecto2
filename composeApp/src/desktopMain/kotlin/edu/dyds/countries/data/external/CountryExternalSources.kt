package edu.dyds.countries.data.external

import edu.dyds.countries.domain.entity.Country

interface CountryDetailExternalSource {
    suspend fun getCountryById(id: String): Country?
}

interface AllCountriesExternalSource {
    suspend fun getAllCountries(): List<Country>
}
