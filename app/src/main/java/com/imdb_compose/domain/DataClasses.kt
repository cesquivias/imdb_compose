package com.imdb_compose.domain

import java.util.Date

data class TvList(
    val results: List<TvResult>
)

data class TvResult(
    val id: Int,
    val name: String,
    val original_name: String,
    val overview: String,
    val poster_path: String,
    val backdrop_path: String,
    val popularity: String,
    val vote_average: String,
    val first_air_date: String
)

data class ActorList(
    val results: List<ActorResult>
)

data class ActorResult(
    val id: Int,
    val name: String,
    val original_name: String,
    val known_for_department: String?,
    val profile_path: String,
    val popularity: String,
    val vote_average: String,
    val known_for: List<MovieResult>?
)

data class ActorDetail(
    val name: String,
    val id: Int,
    val biography: String,
    val birthday: String,
    val deathDay: String,
    val place_of_birth: String,
    val profile_path: String,
    val popularity: String,
    val known_for_department: String?
)

data class MovieList(
    val results: List<MovieResult>
)

data class MovieResult(
    val id: Int,
    val title: String,
    val original_title: String,
    val overview: String,
    val backdrop_path: String,
    val poster_path: String,
    val popularity: String,
    val vote_average: String,
    val release_date: Date
)

data class MovieDetail(
    val id: Int,
    val imdb_id: String,
    val title: String,
    val original_title: String,
    val adult: Boolean,
    val backdrop_path: String,
    val genres: List<Genre>,
    val belongs_to_collection: MovieCollection,
    val budget: Int,
    val overview: String,
    val popularity: String,
    val poster_path: String,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val status: String,
    val tagline: String,
    val vote_average: String,
    val vote_count: Int
)

data class Genre(
    val id: Int,
    val name: String
)

data class MovieCollection(
    val id: Int,
    val name: String,
    val poster_path: String,
    val backdrop_path: String
)

data class Images(
    val id: Int,
    val backdrops: List<ImageResult>,
    val posters: List<ImageResult>,
    val logos: List<ImageResult>
)

data class ImageResult(
    val aspect_ratio: Float,
    val height: Int,
    val file_path: String,
    val vote_average: Float,
    val vote_count: Int,
    val width: Int
)

data class TvDetails(
    val id: Int,
    val name: String,
    val adult: Boolean,
    val backdrop_path: String,
    val created_by: List<Any>,
    val first_air_date: String,
    val last_air_date: String,
    val genres: List<Genre>,
    val in_production: String,
    val languages: List<String>,
    val last_episode_to_air: EpisodeToAir,
    val next_episode_to_air: EpisodeToAir,
    val networks: List<Network>,
    val number_of_episodes: Int,
    val number_of_seasons: Int,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Float,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<Countries>,
    val seasons: List<Season>,
    val spoken_languages: List<Language>,
    val status: String,
    val tagline: String,
    val type: String,
    val vote_average: Float,
    val vote_count: Int
)

data class EpisodeToAir(
    val id: Int,
    val name: String,
    val overview: String,
    val vote_average: Float,
    val vote_count: Int,
    val air_date: String,
    val episode_number: Int,
    val episode_type: String,
    val production_code: String,
    val runtime: Int,
    val season_number: Int,
    val show_id: Int,
    val still_path: String
)

data class Network(
    val id: Int,
    val logo_path: String,
    val name: String,
    val origin_country: String
)

data class ProductionCompany(
    val id: Int,
    val logo_path: String,
    val name: String,
    val origin_country: String
)

data class Countries(
    val name: String
)

data class Season(
    val air_date: String,
    val episode_count: Int,
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String,
    val season_number: Int,
    val vote_average: Float
)

data class Language(
    val english_name: String,
    val name: String
)

data class MovieSearch(
    val results: List<Search>,
    val total_pages: Int,
    val total_results: Int
)

data class TvSearch(
    val results: List<Search>,
    val total_pages: Int,
    val total_results: Int
)

data class PersonSearch(
    val results: List<Search>,
    val total_pages: Int,
    val total_results: Int
)

data class MultiSearch(
    val results: List<Search>,
    val total_pages: Int,
    val total_results: Int
)

data class Search(
    val id: Int,
    val adult: Boolean,
    val gender: Int?,
    val backdrop_path: String?,
    val poster_path: String?,
    val profile_path: String?,
    val genre_id: List<Int>?,
    val title: String?,
    val name: String?,
    val original_name: String?,
    val original_title: String?,
    val original_language: String?,
    val overview: String?,
    val popularity: Float,
    val vote_average: Float?,
    val release_date: String?,
    val first_air_date: String?,
    val known_for_department: String?,
    val known_for: List<KnownFor>?
)

data class KnownFor(
    val backdrop_path: String,
    val id: Int,
    val title: String,
    val original_title: String,
    val overview: String,
    val poster_path: String,
    val media_type: String,
    val adult: Boolean,
    val original_language: String,
    val genre_ids: List<Int>,
    val popularity: Float,
    val release_date: String,
    val vote_average: Float
)

data class VideoClipList(
    val id: Int,
    val results: List<Clip>
)

data class Clip(
    val id: String,
    val name: String,
    val key: String,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    val published_at: String
)
