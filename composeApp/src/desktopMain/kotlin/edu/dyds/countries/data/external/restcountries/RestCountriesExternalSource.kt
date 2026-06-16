package edu.dyds.countries.data.external.restcountries

import edu.dyds.countries.data.external.CountriesSearchExternalSource
import edu.dyds.countries.data.external.CountryDetailExternalSource
import edu.dyds.countries.domain.entity.Country
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

private const val BASE_URL = "https://api.restcountries.com/countries/v5"
private const val API_KEY = "rc_live_87079071953942419c75bc712890f652"

class RestCountriesExternalSource(
    private val httpClient: HttpClient
) : CountriesSearchExternalSource, CountryDetailExternalSource {

    override suspend fun searchCountries(query: String): List<Country> {
        return if (query.isBlank()) {
            getAllCountries()
        } else {
            getCountriesByQuery(query)
        }
    }

    override suspend fun getCountryById(id: String): Country? {
        val response: RestCountriesResponse = httpClient.get("$BASE_URL/codes.alpha_3/$id") {
            header("Authorization", "Bearer $API_KEY")
        }.body()
        return response.data.objects.firstOrNull()?.toDomain()
    }

    private suspend fun getCountriesByQuery(query: String): List<Country> {
        val response: RestCountriesResponse = httpClient.get("$BASE_URL/name") {
            header("Authorization", "Bearer $API_KEY")
            parameter("q", query)
        }.body()
        return response.data.objects.map { it.toDomain() }
    }

    private suspend fun getAllCountries(): List<Country> {
        val response: RestCountriesResponse = httpClient.get("$BASE_URL") {
            header("Authorization", "Bearer $API_KEY")
        }.body()
        return response.data.objects.map { it.toDomain() }
    }
}