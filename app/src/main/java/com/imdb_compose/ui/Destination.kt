package com.imdb_compose.ui

import kotlinx.serialization.Serializable

interface Destination {
    @Serializable
    data object HomePage: Destination
    @Serializable
    data object SearchPage: Destination
    @Serializable
    data object PlayClipPage: Destination
    @Serializable
    data class CategoryPage(val catagory: String): Destination
    @Serializable
    data class MovieDetailsPage(val id: Int, val title: String, val catagory: String): Destination
    @Serializable
    data class PersonDetailsPage(val id: Int, val name: String, val catagory: String): Destination
    @Serializable
    data class TvDetailsPage(val id: Int, val show: String, val catagory: String): Destination
}
