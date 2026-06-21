package edu.dyds.countries.data.repository

import edu.dyds.countries.data.external.CountriesListExternalSource
import edu.dyds.countries.data.external.CountryDetailExternalSource
import edu.dyds.countries.data.local.CountriesLocalDataSource
import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.Currency
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import java.lang.RuntimeException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CountriesRepositoryImplTest {

    private lateinit var countriesListExternalSource: CountriesListExternalSource
    private lateinit var countryDetailExternalSource: CountryDetailExternalSource
    private lateinit var localDataSource: CountriesLocalDataSource
    private lateinit var repository: CountriesRepositoryImpl

    @BeforeTest
    fun setup() {
        countriesListExternalSource = mockk()
        countryDetailExternalSource = mockk()
        localDataSource = mockk()
        repository = CountriesRepositoryImpl(
            countriesListExternalSource = countriesListExternalSource,
            countryDetailExternalSource = countryDetailExternalSource,
            localDataSource = localDataSource
        )
    }

    @Test
    fun `si hay cache devuelve la cache y no consulta remoto`() = runTest {
        val cachedCountries = listOf(country(name = "Argentina"), country(name = "Brazil"))
        coEvery { localDataSource.getAllCountries() } returns cachedCountries

        val result = repository.getAllCountries()

        assertEquals(cachedCountries, result)
        coVerify(exactly = 0) { countriesListExternalSource.getAllCountries() }
    }

    @Test
    fun `si no hay cache consulta remoto guarda en cache y devuelve`() = runTest {
        val remoteCountries = listOf(country(name = "France"), country(name = "Germany"))
        coEvery { localDataSource.getAllCountries() } returns emptyList()
        coEvery { countriesListExternalSource.getAllCountries() } returns remoteCountries
        coEvery { localDataSource.saveCountries(remoteCountries) } returns Unit

        val result = repository.getAllCountries()

        assertEquals(remoteCountries, result)
        coVerify(exactly = 1) { countriesListExternalSource.getAllCountries() }
        coVerify(exactly = 1) { localDataSource.saveCountries(remoteCountries) }
    }

    @Test
    fun `si no hay cache y remoto falla devuelve lista vacia sin guardar`() = runTest {
        coEvery { localDataSource.getAllCountries() } returns emptyList()
        coEvery { countriesListExternalSource.getAllCountries() } throws RuntimeException("Network error")

        val result = repository.getAllCountries()

        assertEquals(emptyList(), result)
        coVerify(exactly = 0) { localDataSource.saveCountries(any()) }
    }

    @Test
    fun `si detalle remoto devuelve un pais lo retorna sin consultar cache`() = runTest {
        val remoteCountry = country(id = "ARG", name = "Argentina")
        coEvery { countryDetailExternalSource.getCountryById("ARG") } returns remoteCountry

        val result = repository.getCountryById("ARG")

        assertEquals(remoteCountry, result)
        coVerify(exactly = 1) { countryDetailExternalSource.getCountryById("ARG") }
        coVerify(exactly = 0) { localDataSource.getCountryById(any()) }
    }

    @Test
    fun `si detalle remoto falla usa cache local como fallback`() = runTest {
        val cachedCountry = country(id = "BRA", name = "Brazil")
        coEvery { countryDetailExternalSource.getCountryById("BRA") } throws RuntimeException("boom")
        coEvery { localDataSource.getCountryById("BRA") } returns cachedCountry

        val result = repository.getCountryById("BRA")

        assertEquals(cachedCountry, result)
        coVerify(exactly = 1) { countryDetailExternalSource.getCountryById("BRA") }
        coVerify(exactly = 1) { localDataSource.getCountryById("BRA") }
    }

    @Test
    fun `si detalle remoto y cache fallan devuelve null`() = runTest {
        coEvery { countryDetailExternalSource.getCountryById("XYZ") } throws RuntimeException("fail")
        coEvery { localDataSource.getCountryById("XYZ") } throws RuntimeException("fail")

        val result = repository.getCountryById("XYZ")

        assertNull(result)
        coVerify(exactly = 1) { countryDetailExternalSource.getCountryById("XYZ") }
        coVerify(exactly = 1) { localDataSource.getCountryById("XYZ") }
    }

    private fun country(
        id: String = "ID",
        name: String = "Name",
        region: String = "Region",
        languages: List<String> = listOf("Spanish")
    ): Country = Country(
        id = id,
        name = name,
        officialName = "Argentine Republic",
        capital = "Buenos Aires",
        region = region,
        subregion = "South America",
        population = 46000000,
        areaKm2 = 2780400.0,
        currencies = listOf(
            Currency(code = "ARS", name = "Argentine Peso", symbol = "$")
        ),
        languages = languages,
        flagPng = "https://flagcdn.com/w320/ar.png",
        flagEmoji = "🇦🇷",
        capitalLatitude = -34.6037,
        capitalLongitude = -58.3816
    )
}
