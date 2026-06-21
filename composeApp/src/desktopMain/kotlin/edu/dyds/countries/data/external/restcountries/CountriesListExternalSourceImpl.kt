package edu.dyds.countries.data.external.restcountries

import edu.dyds.countries.data.external.CountriesListExternalSource
import edu.dyds.countries.domain.entity.Country

class CountriesListExternalSourceImpl(
    private val restCountriesExternalSource: RestCountriesExternalSource
) : CountriesListExternalSource {

    override suspend fun getAllCountries(): List<Country> {
        return restCountriesExternalSource.getAllCountries().map { it.toDomain() }
    }
}
