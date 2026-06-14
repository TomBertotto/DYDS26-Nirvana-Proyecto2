package edu.dyds.countries.data.external

import edu.dyds.countries.domain.entity.Country

class CountryExternalSourceBroker(
    private val openMeteoSource: CountryDetailExternalSource,
    private val restCountriesSource: CountryDetailExternalSource
) : CountryDetailExternalSource {

    override suspend fun getCountryById(id: String): Country? {
        val openMeteoCountry = runCatching { openMeteoSource.getCountryById(id) }.getOrNull()
        val restCountriesCountry = runCatching { restCountriesSource.getCountryById(id) }.getOrNull()

        return when {
            openMeteoCountry != null && restCountriesCountry != null -> mergeCountries(openMeteoCountry, restCountriesCountry)
            openMeteoCountry != null -> openMeteoCountry.copy(description = "OpenMeteo: ${openMeteoCountry.description}")
            restCountriesCountry != null -> restCountriesCountry.copy(description = "RestCountries: ${restCountriesCountry.description}")
            else -> null
        }
    }

    private fun mergeCountries(openMeteo: Country, restCountries: Country): Country {
        return restCountries.copy(
            description = "RestCountries: ${restCountries.description}\n\nOpenMeteo: ${openMeteo.description}",
            calories = (openMeteo.calories + restCountries.calories) / 2,
            rating = (openMeteo.rating + restCountries.rating) / 2.0
        )
    }
}
