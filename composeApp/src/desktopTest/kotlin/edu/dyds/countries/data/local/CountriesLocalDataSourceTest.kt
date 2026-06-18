package edu.dyds.countries.data.local

import edu.dyds.countries.domain.entity.Country
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CountriesLocalDataTest {

    private val defaultCountry = Country(
        id = "ARG",
        name = "Argentina",
        officialName = "Argentine Republic",
        capital = "Buenos Aires",
        region = "Americas",
        subregion = "South America",
        population = 46000000L,
        areaKm2 = 2780400.0,
        currencies = emptyList(),
        languages = listOf("Spanish"),
        flagPng = "https://flagcdn.com/w320/ar.png",
        flagEmoji = "🇦🇷",
        capitalLatitude = -34.6037,
        capitalLongitude = -58.3816
    )

    private lateinit var localDataSource: CountriesLocalDataSourceImpl

    @Before
    fun setUp() {
        localDataSource = CountriesLocalDataSourceImpl()
    }

    @Test
    fun `dado que no se guardan paises, al consultar cache retorna lista vacia`() = runTest {
        assertTrue(localDataSource.getAllCountries().isEmpty())
    }

    @Test
    fun `dada una cache vacia, al guardar paises, la cache los contiene`() = runTest {
        val countries = listOf(
            defaultCountry.copy(id = "ARG", name = "Argentina"),
            defaultCountry.copy(id = "BRA", name = "Brazil")
        )

        localDataSource.saveCountries(countries)

        assertEquals(countries, localDataSource.getAllCountries())
    }

    @Test
    fun `dada una cache con paises, cuando se guarda una lista nueva de paises, se reemplaza el cache anterior`() = runTest {
        localDataSource.saveCountries(listOf(defaultCountry.copy(id = "ARG"), defaultCountry.copy(id = "BRA")))
        val secondBatch = listOf(defaultCountry.copy(id = "CHL", name = "Chile"), defaultCountry.copy(id = "URY", name = "Uruguay"))

        localDataSource.saveCountries(secondBatch)

        assertEquals(secondBatch, localDataSource.getAllCountries())
    }

    @Test
    fun `dada una cache con paises, al guardar una lista vacia, entonces la cache queda vacia`() = runTest {
        localDataSource.saveCountries(listOf(defaultCountry.copy(id = "ARG")))

        localDataSource.saveCountries(emptyList())

        assertTrue(localDataSource.getAllCountries().isEmpty())
    }

    @Test
    fun `en una cache con varios paises, al buscar por un id existente, retorna el pais correcto`() = runTest {
        val targetCountry = defaultCountry.copy(id = "MEX", name = "Mexico")
        localDataSource.saveCountries(listOf(defaultCountry.copy(id = "ARG"), targetCountry, defaultCountry.copy(id = "BRA")))

        val result = localDataSource.getCountryById("MEX")

        assertEquals(targetCountry, result)
    }

    @Test
    fun `dado un id que no existe en cache, al buscar por ese id, retorna null`() = runTest {
        localDataSource.saveCountries(listOf(defaultCountry.copy(id = "ARG"), defaultCountry.copy(id = "BRA")))

        val result = localDataSource.getCountryById("USA")

        assertNull(result)
    }

    @Test
    fun `dada una cache vacia, al buscar un pais por id, retorna null`() = runTest {
        val result = localDataSource.getCountryById("ANY")

        assertNull(result)
    }
}