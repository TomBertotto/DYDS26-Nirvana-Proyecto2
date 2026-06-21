package edu.dyds.countries.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import edu.dyds.countries.domain.entity.Country
import kotlinx.coroutines.flow.first
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CountriesLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>
) : CountriesLocalDataSource {

    private val json = Json { ignoreUnknownKeys = true }
    private var cachedCountries: List<Country>? = null

    override suspend fun saveCountries(countries: List<Country>) {
        val serialized = json.encodeToString(countries.map { it.toLocal() })
        dataStore.edit { preferences ->
            preferences[COUNTRIES_KEY] = serialized
        }
        cachedCountries = null
    }

    override suspend fun getCountryById(id: String): Country? {
        return getAllCountries().firstOrNull { it.id == id }
    }

    override suspend fun getAllCountries(): List<Country> {
        cachedCountries?.let { return it }

        val serialized = dataStore.data.first()[COUNTRIES_KEY] ?: return emptyList()
        val countries = json.decodeFromString<List<CountryLocal>>(serialized).map { it.toDomain() }
        cachedCountries = countries
        return countries
    }

    companion object {
        private val COUNTRIES_KEY = stringPreferencesKey("countries")
    }
}
