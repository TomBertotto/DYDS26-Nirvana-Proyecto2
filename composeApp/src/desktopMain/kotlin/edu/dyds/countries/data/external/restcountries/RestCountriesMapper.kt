package edu.dyds.countries.data.external.restcountries

import edu.dyds.countries.domain.entity.Country

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
        flagPng = flag.urlPng,
        flagEmoji = flag.emoji,
        capitalLatitude = capital?.coordinates?.lat,
        capitalLongitude = capital?.coordinates?.lng
    )
}
