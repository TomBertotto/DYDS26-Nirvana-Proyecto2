package edu.dyds.countries.data.external.restcountries

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.Currency
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CountriesSearchExternalSourceImplTest {

    private lateinit var restCountriesExternalSource: RestCountriesExternalSource
    private lateinit var externalSource: CountriesSearchExternalSourceImpl

    @BeforeTest
    fun setup() {
        restCountriesExternalSource = mockk()
        externalSource = CountriesSearchExternalSourceImpl(restCountriesExternalSource)
    }

    @Test
    fun `searchCountries convierte la lista remota de RestCountries a lista de Country`() = runTest {
        val remoteCountry = createRemoteCountryMock(
            alpha3 = "ARG",
            commonName = "Argentina",
            officialName = "Argentine Republic"
        )
        coEvery { restCountriesExternalSource.searchCountries("Argentina") } returns listOf(remoteCountry)

        val result = externalSource.searchCountries("Argentina")

        val expected = Country(
            id = "ARG",
            name = "Argentina",
            officialName = "Argentine Republic",
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

        assertEquals(listOf(expected), result)
    }
}
