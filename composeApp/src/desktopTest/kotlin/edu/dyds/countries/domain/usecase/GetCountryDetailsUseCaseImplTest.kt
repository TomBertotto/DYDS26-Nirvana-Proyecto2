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
import kotlin.test.assertNull

class GetCountryDetailsUseCaseImplTest {

    private lateinit var repository: CountriesRepository
    private lateinit var useCase: GetCountryDetailsUseCaseImpl

    @BeforeTest
    fun setup() {
        repository = mockk()
        useCase = GetCountryDetailsUseCaseImpl(repository)
    }

    @Test
    fun `si existe el pais delega la busqueda por id y devuelve el detalle`() = runTest {
        val expectedCountry = country(id = "ARG", name = "Argentina")
        coEvery { repository.getCountryById("ARG") } returns expectedCountry

        val result = useCase("ARG")

        assertEquals(expectedCountry, result)
        coVerify(exactly = 1) { repository.getCountryById("ARG") }
    }

    @Test
    fun `si no existe el pais devuelve null`() = runTest {
        coEvery { repository.getCountryById("XYZ") } returns null

        val result = useCase("XYZ")

        assertNull(result)
        coVerify(exactly = 1) { repository.getCountryById("XYZ") }
    }

    private fun country(
        id: String = "ID",
        name: String = "Name"
    ): Country = Country(
        id = id,
        name = name,
        officialName = "Argentine Republic",
        capital = "Buenos Aires",
        region = "Americas",
        subregion = "South America",
        population = 46000000,
        areaKm2 = 2780400.0,
        currencies = listOf(
            Currency(code = "ARS", name = "Argentine Peso", symbol = "$")
        ),
        languages = listOf("Spanish"),
        flagPng = "https://flagcdn.com/w320/ar.png",
        flagEmoji = "🇦🇷",
        capitalLatitude = -34.6037,
        capitalLongitude = -58.3816
    )
}
