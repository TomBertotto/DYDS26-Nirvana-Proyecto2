package edu.dyds.countries.data.external.restcountries

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

private const val BASE_URL = "https://api.restcountries.com/countries/v5"
private const val API_KEY = "rc_live_0e3b86b282454e8895aa19549b256e07"
private const val AUTHORIZATION_HEADER = "Authorization"
private const val BEARER_PREFIX = "Bearer "
private const val LIMIT_PARAMETER = "limit"
private const val OFFSET_PARAMETER = "offset"
private const val PAGE_SIZE = 100
private const val FIRST_OFFSET = 1

class RestCountriesExternalSource(
    private val httpClient: HttpClient
) {
    suspend fun getAllCountries(): List<RestCountriesRemoteCountry> {
        val allCountries = mutableListOf<RestCountriesRemoteCountry>()
        var offset = FIRST_OFFSET
        var hasMorePages = true

        while (hasMorePages) {
            val countriesFromPage = getCountriesPage(offset)
            allCountries.addAll(countriesFromPage)
            offset += PAGE_SIZE
            hasMorePages = countriesFromPage.size >= PAGE_SIZE
        }

        return allCountries
    }

    suspend fun getCountryById(id: String): RestCountriesRemoteCountry? {
        val response: RestCountriesResponse = httpClient.get("$BASE_URL/codes.alpha_3/$id") {
            header(AUTHORIZATION_HEADER, "$BEARER_PREFIX$API_KEY")
        }.body()
        return response.data.objects.firstOrNull()
    }

    private suspend fun getCountriesPage(offset: Int): List<RestCountriesRemoteCountry> {
        val response: RestCountriesResponse = httpClient.get(BASE_URL) {
            header(AUTHORIZATION_HEADER, "$BEARER_PREFIX$API_KEY")
            parameter(LIMIT_PARAMETER, PAGE_SIZE)
            parameter(OFFSET_PARAMETER, offset)
        }.body()
        return response.data.objects
    }
}
