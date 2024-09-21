package com.imdb_compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CatagoryPageViewModel(category: String) : ViewModel() {
    val BASE_URL = "https://api.themoviedb.org/"

    private val movieApit = Retrofit.getInstance().create(MovieApi::class.java)
    private val tvApi = Retrofit.getInstance().create(TvApi::class.java)

    private val _trendingTv: MutableStateFlow<TvList?> = MutableStateFlow(null)
    val trendingTv: StateFlow<TvList?> = _trendingTv.asStateFlow()

    private val _tvImgs: MutableStateFlow<ImageResults?> = MutableStateFlow(null)
    val tvImgs: StateFlow<ImageResults?> = _tvImgs.asStateFlow()


    /**
     * "backdrops": [
     *     {
     *       "aspect_ratio": 1.777,
     *       "height": 1688,
     *       "iso_639_1": null,
     *       "file_path": "/AvH03Lj5lMYxmlPc7prNQLWw6JY.jpg",
     *       "vote_average": 5.714,
     *       "vote_count": 7,
     *       "width": 3000
     *     },
     *     ...
     */

    init {
        // https://api.themoviedb.org/3/tv/{series_id}/images
    }

    suspend fun getTvImage(id: Int, imgPath: String) {
        viewModelScope.launch {
            val result = tvApi.getTvImg(id, imgPath)
            _tvImgs.value = result
        }
    }

}

data class ImageResults(
    val backdrops: List<Img>
)

data class Img(
    val file_path: String,
    val height: Int,
    val width: Int,
    val aspect_ratio: Float
)
