package dev.davron.regionaltaxi.models.city

import java.io.Serializable

data class CitySelectedLocationModel(
    val name: String,
    val latLng: String
) : Serializable