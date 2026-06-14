package edu.dyds.countries.data.external.restcountries

import edu.dyds.countries.domain.entity.Country

fun RestCountriesRemoteCountry.toDomain(): Country = Country(
    id = codes.alpha3.ifBlank { names.common },
    name = names.common,
    officialName = names.official,
    capital = capitals.firstOrNull()?.name.orEmpty(),
    region = region,
    subregion = subregion,
    population = population,
    flagPng = flag.urlPng,
    flagEmoji = flag.emoji
)
