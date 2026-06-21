package edu.dyds.countries.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import edu.dyds.countries.domain.entity.Country
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.runTest
import okio.Path.Companion.toPath
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

class CountriesLocalDataSourceImplTest {

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

    private lateinit var tempDirectory: File
    private lateinit var dataStoreScope: CoroutineScope
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var localDataSource: CountriesLocalDataSourceImpl

    @Before
    fun setUp() {
        tempDirectory = File.createTempFile("countries-test", "").apply {
            delete()
            mkdirs()
        }
        dataStoreScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        dataStore = PreferenceDataStoreFactory.createWithPath(scope = dataStoreScope) {
            File(tempDirectory, "test.preferences_pb").absolutePath.toPath()
        }
        localDataSource = CountriesLocalDataSourceImpl(dataStore)
    }

    @After
    fun tearDown() {
        dataStoreScope.cancel()
        tempDirectory.deleteRecursively()
    }

    @Test
    fun `dado que no se guardan paises, al consultar retorna lista vacia`() = runTest {
        assertTrue(localDataSource.getAllCountries().isEmpty())
    }

    @Test
    fun `al guardar paises, al consultar los contiene`() = runTest {
        val countries = listOf(
            defaultCountry.copy(id = "ARG", name = "Argentina"),
            defaultCountry.copy(id = "BRA", name = "Brazil")
        )

        localDataSource.saveCountries(countries)

        assertEquals(countries, localDataSource.getAllCountries())
    }

    @Test
    fun `al guardar una lista nueva, se reemplaza la anterior`() = runTest {
        localDataSource.saveCountries(listOf(defaultCountry.copy(id = "ARG"), defaultCountry.copy(id = "BRA")))
        val secondBatch = listOf(defaultCountry.copy(id = "CHL", name = "Chile"), defaultCountry.copy(id = "URY", name = "Uruguay"))

        localDataSource.saveCountries(secondBatch)

        assertEquals(secondBatch, localDataSource.getAllCountries())
    }

    @Test
    fun `al guardar una lista vacia, la consulta queda vacia`() = runTest {
        localDataSource.saveCountries(listOf(defaultCountry.copy(id = "ARG")))

        localDataSource.saveCountries(emptyList())

        assertTrue(localDataSource.getAllCountries().isEmpty())
    }

    @Test
    fun `al buscar por un id existente, retorna el pais correcto`() = runTest {
        val targetCountry = defaultCountry.copy(id = "MEX", name = "Mexico")
        localDataSource.saveCountries(listOf(defaultCountry.copy(id = "ARG"), targetCountry, defaultCountry.copy(id = "BRA")))

        val result = localDataSource.getCountryById("MEX")

        assertEquals(targetCountry, result)
    }

    @Test
    fun `dado un id que no existe, al buscar retorna null`() = runTest {
        localDataSource.saveCountries(listOf(defaultCountry.copy(id = "ARG"), defaultCountry.copy(id = "BRA")))

        val result = localDataSource.getCountryById("USA")

        assertNull(result)
    }

    @Test
    fun `dada una consulta vacia, al buscar por id retorna null`() = runTest {
        val result = localDataSource.getCountryById("ANY")

        assertNull(result)
    }
}
