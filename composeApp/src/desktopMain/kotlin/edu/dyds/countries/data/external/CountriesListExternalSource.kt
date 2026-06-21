package edu.dyds.countries.data.external

import edu.dyds.countries.domain.entity.Country

interface CountriesListExternalSource {
    suspend fun getAllCountries(): List<Country>
}
