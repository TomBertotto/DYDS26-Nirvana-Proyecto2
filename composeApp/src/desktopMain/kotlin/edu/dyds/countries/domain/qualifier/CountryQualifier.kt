package edu.dyds.countries.domain.qualifier

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.QualifiedCountry

class CountryQualifier {
    fun qualifyCountries(countries: List<Country>): List<QualifiedCountry> {
        return countries
            .filter { it.rating >= 4.0 }
            .map { QualifiedCountry(it, isGoodCountry = true) }
            .sortedByDescending { it.country.rating }
    }
}
