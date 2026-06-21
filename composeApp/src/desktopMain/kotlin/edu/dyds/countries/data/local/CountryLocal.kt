package edu.dyds.countries.data.local

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.Currency
import kotlinx.serialization.Serializable

@Serializable
data class CountryLocal(
    val id: String,
    val name: String,
    val officialName: String,
    val capital: String,
    val region: String,
    val subregion: String,
    val population: Long,
    val areaKm2: Double?,
    val currencies: List<CurrencyLocal>,
    val languages: List<String>,
    val flagPng: String,
    val flagEmoji: String,
    val capitalLatitude: Double?,
    val capitalLongitude: Double?
)

@Serializable
data class CurrencyLocal(
    val code: String,
    val name: String,
    val symbol: String
)

fun Country.toLocal(): CountryLocal = CountryLocal(
    id = id,
    name = name,
    officialName = officialName,
    capital = capital,
    region = region,
    subregion = subregion,
    population = population,
    areaKm2 = areaKm2,
    currencies = currencies.map { CurrencyLocal(it.code, it.name, it.symbol) },
    languages = languages,
    flagPng = flagPng,
    flagEmoji = flagEmoji,
    capitalLatitude = capitalLatitude,
    capitalLongitude = capitalLongitude
)

fun CountryLocal.toDomain(): Country = Country(
    id = id,
    name = name,
    officialName = officialName,
    capital = capital,
    region = region,
    subregion = subregion,
    population = population,
    areaKm2 = areaKm2,
    currencies = currencies.map { Currency(it.code, it.name, it.symbol) },
    languages = languages,
    flagPng = flagPng,
    flagEmoji = flagEmoji,
    capitalLatitude = capitalLatitude,
    capitalLongitude = capitalLongitude
)
