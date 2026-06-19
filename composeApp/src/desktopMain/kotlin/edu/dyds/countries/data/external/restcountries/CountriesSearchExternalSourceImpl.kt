package edu.dyds.countries.data.external.restcountries

import edu.dyds.countries.data.external.CountriesSearchExternalSource
import edu.dyds.countries.domain.entity.Country

class CountriesSearchExternalSourceImpl(
    private val restCountriesExternalSource: RestCountriesExternalSource
) : CountriesSearchExternalSource {

    override suspend fun searchCountries(query: String): List<Country> {
        return restCountriesExternalSource.searchCountries(query).map { it.toDomain() }
    }
}
