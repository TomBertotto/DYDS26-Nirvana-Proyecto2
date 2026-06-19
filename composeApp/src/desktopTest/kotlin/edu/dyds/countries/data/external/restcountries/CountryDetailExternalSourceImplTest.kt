package edu.dyds.countries.data.external.restcountries

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.Currency
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CountryDetailExternalSourceImplTest {

    private lateinit var restCountriesExternalSource: RestCountriesExternalSource
    private lateinit var externalSource: CountryDetailExternalSourceImpl

    @BeforeTest
    fun setup() {
        restCountriesExternalSource = mockk()
        externalSource = CountryDetailExternalSourceImpl(restCountriesExternalSource)
    }

    @Test
    fun `getCountryById convierte el objeto remoto de RestCountries a Country`() = runTest {
        val remoteCountry = createRemoteCountryMock(
            alpha3 = "BRA",
            commonName = "Brazil",
            officialName = "Federative Republic of Brazil"
        )
        coEvery { restCountriesExternalSource.getCountryById("BRA") } returns remoteCountry

        val result = externalSource.getCountryById("BRA")

        val expected = Country(
            id = "BRA",
            name = "Brazil",
            officialName = "Federative Republic of Brazil",
            capital = "Buenos Aires",
            region = "Americas",
            subregion = "South America",
            population = 46000000,
            areaKm2 = 2780400.0,
            currencies = listOf(Currency(code = "ARS", name = "Argentine peso", symbol = "$")),
            languages = listOf("Spanish"),
            flagPng = "https://flagcdn.com/w320/ar.png",
            flagEmoji = "🇦🇷",
            capitalLatitude = -34.6037,
            capitalLongitude = -58.3816
        )

        assertEquals(expected, result)
    }

    @Test
    fun `getCountryById devuelve null cuando no hay resultado remoto`() = runTest {
        coEvery { restCountriesExternalSource.getCountryById("XXX") } returns null

        val result = externalSource.getCountryById("XXX")

        assertNull(result)
    }
}
