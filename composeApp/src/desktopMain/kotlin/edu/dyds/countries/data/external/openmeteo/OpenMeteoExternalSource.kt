package edu.dyds.countries.data.external.openmeteo

import edu.dyds.countries.data.external.CountryDetailExternalSource
import edu.dyds.countries.domain.entity.Country

class OpenMeteoExternalSource : CountryDetailExternalSource {
    override suspend fun getCountryById(id: String): Country? {
        return Country(
            id = id,
            name = "Healthy Bowl",
            description = "Sample country",
            ingredients = listOf("rice", "vegetables", "protein"),
            instructions = "Mix and serve",
            image = "",
            servings = 1,
            prepTime = 5,
            cookTime = 10,
            calories = 350,
            rating = 4.2
        )
    }
}
