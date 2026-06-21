package edu.dyds.countries.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath
import java.io.File

private const val DATA_STORE_DIRECTORY = "local_storage"
private const val DATA_STORE_FILE = "countries.preferences_pb"

fun createCountriesDataStore(): DataStore<Preferences> {
    val directory = File(DATA_STORE_DIRECTORY)
    directory.mkdirs()
    val file = File(directory, DATA_STORE_FILE)
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { file.absolutePath.toPath() }
    )
}
