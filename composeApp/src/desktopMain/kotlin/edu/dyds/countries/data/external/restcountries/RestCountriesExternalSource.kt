package edu.dyds.countries.data.external.restcountries

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

private const val BASE_URL = "https://api.restcountries.com/countries/v5"
private const val API_KEY = "rc_live_87079071953942419c75bc712890f652"

class RestCountriesExternalSource(
    private val httpClient: HttpClient
){
    suspend fun searchCountries(query: String): List<RestCountriesRemoteCountry> {
        return if (query.isBlank()) {
            getAllCountries()
        } else {
            getCountriesByQuery(query)
        }
    }

    suspend fun getCountryById(id: String): RestCountriesRemoteCountry? {
        val response: RestCountriesResponse = httpClient.get("$BASE_URL/codes.alpha_3/$id") {
            header("Authorization", "Bearer $API_KEY")
        }.body()
        return response.data.objects.firstOrNull()
    }

    private suspend fun getCountriesByQuery(query: String): List<RestCountriesRemoteCountry> {
        val response: RestCountriesResponse = httpClient.get("$BASE_URL/name") {
            header("Authorization", "Bearer $API_KEY")
            parameter("q", query)
        }.body()
        return response.data.objects
    }

    private suspend fun getAllCountries(): List<RestCountriesRemoteCountry> {
        val allCountries = mutableListOf<RestCountriesRemoteCountry>()
        var offset = 1
        val limit = 100
        var hasMorePages = true

        while (hasMorePages) {
            val response: RestCountriesResponse = httpClient.get("$BASE_URL") {
                header("Authorization", "Bearer $API_KEY")
                parameter("limit", limit)
                parameter("offset", offset)
            }.body()

            val countriesFromPage = response.data.objects

            if (countriesFromPage.isNotEmpty()) {
                allCountries.addAll(countriesFromPage)
                offset += limit
            }

            if (countriesFromPage.size < limit) {
                hasMorePages = false
            }
        }

        return allCountries
    }
}