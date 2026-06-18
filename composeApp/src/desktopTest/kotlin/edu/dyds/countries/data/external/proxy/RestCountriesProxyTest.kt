package edu.dyds.countries.data.external.proxy

import edu.dyds.countries.data.external.restcountries.RestCountriesCapital
import edu.dyds.countries.data.external.restcountries.RestCountriesCoordinates
import edu.dyds.countries.data.external.restcountries.RestCountriesExternalSource
import edu.dyds.countries.data.external.restcountries.RestCountriesLanguage
import edu.dyds.countries.data.external.restcountries.RestCountriesRemoteCountry
import edu.dyds.countries.domain.entity.Country
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RestCountriesProxyTest {

    private lateinit var countriesSearchMock: RestCountriesExternalSource
    private lateinit var proxy: RestCountriesProxy

    @BeforeTest
    fun setup() {
        countriesSearchMock = mockk()
        proxy = RestCountriesProxy(countriesSearchMock)
    }

    @Test
    fun `searchCountries convierte la lista remota de RestCountries a lista de Country`() = runTest {
        val remoteCountry = createRemoteCountryMock(
            alpha3 = "ARG",
            commonName = "Argentina",
            officialName = "Argentine Republic"
        )
        coEvery { countriesSearchMock.searchCountries("Argentina") } returns listOf(remoteCountry)

        val result = proxy.searchCountries("Argentina")

        val expected = Country(
            id = "ARG",
            name = "Argentina",
            officialName = "Argentine Republic",
            capital = "Buenos Aires",
            region = "Americas",
            subregion = "South America",
            population = 46000000,
            languages = listOf("Spanish"),
            flagPng = "https://flagcdn.com/w320/ar.png",
            flagEmoji = "🇦🇷",
            capitalLatitude = -34.6037,
            capitalLongitude = -58.3816,
            areaKm2 = 2780400.0,
            currencies = emptyList()
        )

        assertEquals(listOf(expected), result)
    }

    @Test
    fun `getCountryById convierte el objeto remoto de RestCountries a Country`() = runTest {
        val remoteCountry = createRemoteCountryMock(
            alpha3 = "BRA",
            commonName = "Brazil",
            officialName = "Federative Republic of Brazil"
        )
        coEvery { countriesSearchMock.getCountryById("BRA") } returns remoteCountry

        val result = proxy.getCountryById("BRA")

        val expected = Country(
            id = "BRA",
            name = "Brazil",
            officialName = "Federative Republic of Brazil",
            capital = "Buenos Aires", // Mismo mock base de capital
            region = "Americas",
            subregion = "South America",
            population = 46000000,
            languages = listOf("Spanish"),
            flagPng = "https://flagcdn.com/w320/ar.png",
            flagEmoji = "🇦🇷",
            capitalLatitude = -34.6037,
            capitalLongitude = -58.3816,
            areaKm2 = 2780400.0,
            currencies = emptyList()
        )

        assertEquals(expected, result)
    }

    private fun createRemoteCountryMock(
        alpha3: String,
        commonName: String,
        officialName: String
    ): RestCountriesRemoteCountry {
        val remote = mockk<RestCountriesRemoteCountry>()

        coEvery { remote.codes.alpha3 } returns alpha3
        coEvery { remote.names.common } returns commonName
        coEvery { remote.names.official } returns officialName
        coEvery { remote.region } returns "Americas"
        coEvery { remote.subregion } returns "South America"
        coEvery { remote.population } returns 46000000L
        coEvery { remote.area.kilometers } returns 2780400.0

        val languageMock = mockk<RestCountriesLanguage>()
        coEvery { languageMock.name } returns "Spanish"
        coEvery { remote.languages } returns listOf(languageMock)

        coEvery { remote.flag.urlPng } returns "https://flagcdn.com/w320/ar.png"
        coEvery { remote.flag.emoji } returns "🇦🇷"

        val coordinatesMock = mockk<RestCountriesCoordinates>()
        coEvery { coordinatesMock.lat } returns -34.6037
        coEvery { coordinatesMock.lng } returns -58.3816

        val capitalMock = mockk<RestCountriesCapital>()
        coEvery { capitalMock.name } returns "Buenos Aires"
        coEvery { capitalMock.coordinates } returns coordinatesMock

        coEvery { remote.capitals } returns listOf(capitalMock)

        return remote
    }
}