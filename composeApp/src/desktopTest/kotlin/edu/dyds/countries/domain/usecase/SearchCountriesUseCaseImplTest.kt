package edu.dyds.countries.domain.usecase

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.Currency
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
    fun `si busca todos delega el filtro all y devuelve todos los paises`() = runTest {
        val expectedCountries = listOf(
            country(name = "Argentina", region = "Americas"),
            country(name = "France", region = "Europe")
        )
        coEvery { repository.searchCountries(query = "", criteria = "All") } returns expectedCountries

        val result = useCase(query = "", filter = "All")

        assertEquals(expectedCountries, result)
        coVerify(exactly = 1) { repository.searchCountries(query = "", criteria = "All") }
    }

    @Test
    fun `si busca por nombre delega el filtro y devuelve los paises encontrados`() = runTest {
        val expectedCountries = listOf(
            country(name = "Argentina", region = "Americas")
        )
        coEvery { repository.searchCountries(query = "Argentina", criteria = "Name") } returns expectedCountries

        val result = useCase(query = "Argentina", filter = "Name")

        assertEquals(expectedCountries, result)
        coVerify(exactly = 1) { repository.searchCountries(query = "Argentina", criteria = "Name") }
    }

    @Test
    fun `si busca por region delega el filtro y devuelve los paises encontrados`() = runTest {
        val expectedCountries = listOf(
            country(name = "France", region = "Europe"),
            country(name = "Germany", region = "Europe")
        )
        coEvery { repository.searchCountries(query = "Europe", criteria = "Region") } returns expectedCountries

        val result = useCase(query = "Europe", filter = "Region")

        assertEquals(expectedCountries, result)
        coVerify(exactly = 1) { repository.searchCountries(query = "Europe", criteria = "Region") }
    }

    @Test
    fun `si busca por lengua delega el filtro y devuelve los paises encontrados`() = runTest {
        val expectedCountries = listOf(
            country(name = "Canada", languages = listOf("English", "French")),
            country(name = "France", languages = listOf("French"))
        )
        coEvery { repository.searchCountries(query = "French", criteria = "Language") } returns expectedCountries

        val result = useCase(query = "French", filter = "Language")

        assertEquals(expectedCountries, result)
        coVerify(exactly = 1) { repository.searchCountries(query = "French", criteria = "Language") }
    }

    @Test
    fun `si el repositorio devuelve lista vacia retorna lista vacia`() = runTest {
        coEvery { repository.searchCountries(query = "Atlantis", criteria = "Name") } returns emptyList()

        val result = useCase(query = "Atlantis", filter = "Name")

        assertEquals(emptyList(), result)
        coVerify(exactly = 1) { repository.searchCountries(query = "Atlantis", criteria = "Name") }
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
