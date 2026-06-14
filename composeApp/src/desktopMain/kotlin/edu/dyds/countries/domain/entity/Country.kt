package edu.dyds.countries.domain.entity

data class Country(
    val id: String,
    val name: String,
    val officialName: String,
    val capital: String,
    val region: String,
    val subregion: String,
    val population: Long,
    val flagPng: String,
    val flagEmoji: String,
    val capitalLatitude: Double?,
    val capitalLongitude: Double?
)
