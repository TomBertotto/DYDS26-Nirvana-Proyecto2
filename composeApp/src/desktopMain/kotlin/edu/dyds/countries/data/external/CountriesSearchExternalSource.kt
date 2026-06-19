package edu.dyds.countries.data.external

import edu.dyds.countries.domain.entity.Country

interface CountriesSearchExternalSource {
    suspend fun searchCountries(query: String): List<Country>
}