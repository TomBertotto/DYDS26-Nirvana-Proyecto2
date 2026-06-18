package edu.dyds.countries.data.external.proxy

import edu.dyds.countries.data.external.CountriesSearchExternalSource
import edu.dyds.countries.data.external.CountryDetailExternalSource
import edu.dyds.countries.data.external.restcountries.RestCountriesExternalSource
import edu.dyds.countries.data.external.restcountries.RestCountriesRemoteCountry
import edu.dyds.countries.domain.entity.Country

class RestCountriesProxy(
    private val countriesSearch: RestCountriesExternalSource
) : CountriesSearchExternalSource, CountryDetailExternalSource{

    override suspend fun searchCountries(query: String): List<Country> {
        return countriesSearch.searchCountries(query).map { it.toDomain() }
    }

    override suspend fun getCountryById(id: String): Country? {
        return countriesSearch.getCountryById(id)?.toDomain()
    }

    fun RestCountriesRemoteCountry.toDomain(): Country {
        val capital = capitals.firstOrNull()
        return Country(
            id = codes.alpha3.ifBlank { names.common },
            name = names.common,
            officialName = names.official,
            capital = capital?.name.orEmpty(),
            region = region,
            subregion = subregion,
            population = population,
            languages = languages.map { it.name },
            flagPng = flag.urlPng,
            flagEmoji = flag.emoji,
            capitalLatitude = capital?.coordinates?.lat,
            capitalLongitude = capital?.coordinates?.lng,
            areaKm2 = area.kilometers,
            currencies = emptyList()
        )
    }


}