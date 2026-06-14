package edu.dyds.countries.domain.entity

data class Weather(
    val temperature: Double,
    val apparentTemperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val weatherCode: Int,
    val description: String,
    val isDay: Boolean
)
