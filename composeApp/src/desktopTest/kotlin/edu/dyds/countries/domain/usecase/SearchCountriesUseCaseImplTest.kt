package edu.dyds.countries.domain.usecase

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.Currency
import edu.dyds.countries.domain.entity.SearchCriteria
import edu.dyds.countries.domain.repository.CountriesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SearchCountriesUseCaseImplTest {

    private lateinit var repository: CountriesRepository
    private lateinit var useCase: SearchCountriesUseCaseImpl

    @BeforeTest
    fun setup() {
        repository = mockk()
        useCase = SearchCountriesUseCaseImpl(repository)
    }

    @Test
    fun `con criterio all devuelve todos los paises`() = runTest {
        val countries = listOf(
            country(name = "Argentina", region = "Americas"),
            country(name = "France", region = "Europe")
        )
        coEvery { repository.getAllCountries() } returns countries

        val result = useCase(query = "", criteria = SearchCriteria.ALL)

        assertEquals(countries, result)
        coVerify(exactly = 1) { repository.getAllCountries() }
    }

    @Test
    fun `con criterio name filtra por nombre`() = runTest {
        val countries = listOf(
            country(name = "Argentina", region = "Americas"),
            country(name = "France", region = "Europe")
        )
        coEvery { repository.getAllCountries() } returns countries

        val result = useCase(query = "arg", criteria = SearchCriteria.NAME)

        assertEquals(listOf(countries[0]), result)
    }

    @Test
    fun `con criterio region filtra por region`() = runTest {
        val countries = listOf(
            country(name = "Argentina", region = "Americas"),
            country(name = "France", region = "Europe"),
            country(name = "Germany", region = "Europe")
        )
        coEvery { repository.getAllCountries() } returns countries

        val result = useCase(query = "Europe", criteria = SearchCriteria.REGION)

        assertEquals(listOf(countries[1], countries[2]), result)
    }

    @Test
    fun `con criterio language filtra por lengua`() = runTest {
        val countries = listOf(
            country(name = "Canada", languages = listOf("English", "French")),
            country(name = "France", languages = listOf("French")),
            country(name = "Brazil", languages = listOf("Portuguese"))
        )
        coEvery { repository.getAllCountries() } returns countries

        val result = useCase(query = "french", criteria = SearchCriteria.LANGUAGE)

        assertEquals(listOf(countries[0], countries[1]), result)
    }

    @Test
    fun `si el repositorio no devuelve paises retorna lista vacia`() = runTest {
        coEvery { repository.getAllCountries() } returns emptyList()

        val result = useCase(query = "Argentina", criteria = SearchCriteria.NAME)

        assertEquals(emptyList(), result)
        coVerify(exactly = 1) { repository.getAllCountries() }
    }

    private fun country(
        id: String = "ID",
        name: String = "Name",
        region: String = "Region",
        languages: List<String> = listOf("Spanish")
    ): Country = Country(
        id = id,
        name = name,
        officialName = "$name Official",
        capital = "Buenos Aires",
        region = region,
        subregion = "South America",
        population = 46000000,
        areaKm2 = 2780400.0,
        currencies = listOf(Currency(code = "ARS", name = "Argentine Peso", symbol = "$")),
        languages = languages,
        flagPng = "https://flagcdn.com/w320/ar.png",
        flagEmoji = "🇦🇷",
        capitalLatitude = -34.6037,
        capitalLongitude = -58.3816
    )
}
