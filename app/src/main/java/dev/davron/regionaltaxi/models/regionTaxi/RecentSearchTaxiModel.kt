package dev.davron.regionaltaxi.models.regionTaxi

data class RecentSearchTaxiModel(
    val fromDistrict: String,
    val fromDistrictId: Int,
    val toDistrict: String,
    val toDistrictId: Int,
)