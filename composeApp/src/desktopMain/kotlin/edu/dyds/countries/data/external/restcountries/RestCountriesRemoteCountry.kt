package edu.dyds.countries.data.external.restcountries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestCountriesResponse(
    val data: RestCountriesData = RestCountriesData()
)

@Serializable
data class RestCountriesData(
    val objects: List<RestCountriesRemoteCountry> = emptyList()
)

@Serializable
data class RestCountriesRemoteCountry(
    val names: RestCountriesNames = RestCountriesNames(),
    val codes: RestCountriesCodes = RestCountriesCodes(),
    val capitals: List<RestCountriesCapital> = emptyList(),
    val flag: RestCountriesFlag = RestCountriesFlag(),
    val region: String = "",
    val subregion: String = "",
    val population: Long = 0,
    val area: RestCountriesArea = RestCountriesArea(),
    val currencies: List<RestCountriesCurrency> = emptyList(),
    val languages: List<RestCountriesLanguage> = emptyList()
)

@Serializable
data class RestCountriesNames(
    val common: String = "",
    val official: String = ""
)

@Serializable
data class RestCountriesCodes(
    @SerialName("alpha_3") val alpha3: String = ""
)

@Serializable
data class RestCountriesCapital(
    val name: String = "",
    val coordinates: RestCountriesCoordinates = RestCountriesCoordinates()
)

@Serializable
data class RestCountriesCoordinates(
    val lat: Double? = null,
    val lng: Double? = null
)

@Serializable
data class RestCountriesFlag(
    val emoji: String = "",
    @SerialName("url_png") val urlPng: String = ""
)

@Serializable
data class RestCountriesArea(
    val kilometers: Double? = null
)

@Serializable
data class RestCountriesCurrency(
    val code: String = "",
    val name: String = "",
    val symbol: String = ""
)

@Serializable
data class RestCountriesLanguage(
    val name: String = ""
)
