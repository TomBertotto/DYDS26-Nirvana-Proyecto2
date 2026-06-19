package edu.dyds.countries.data.external.restcountries

import edu.dyds.countries.data.external.CountryDetailExternalSource
import edu.dyds.countries.domain.entity.Country

class CountryDetailExternalSourceImpl(
    private val restCountriesExternalSource: RestCountriesExternalSource
) : CountryDetailExternalSource {

    override suspend fun getCountryById(id: String): Country? {
        return restCountriesExternalSource.getCountryById(id)?.toDomain()
    }
}
