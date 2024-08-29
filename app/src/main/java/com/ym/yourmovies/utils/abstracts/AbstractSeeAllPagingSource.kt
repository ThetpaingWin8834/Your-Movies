package com.ym.yourmovies.utils.abstracts

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ym.yourmovies.utils.models.SeeAllModel
import com.ym.yourmovies.utils.others.NotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class AbstractSeeAllPagingSource<Movie : Any>(
    private val data: SeeAllModel,
    private val currPage: MutableLiveData<Int>
) : PagingSource<Int, Movie>() {
    abstract suspend fun loadMovies(page: Int): List<Movie>
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        val pos = params.key ?: data.pageAndQuery.page
        return withContext(Dispatchers.IO) {
            try {
                val list = loadMovies(pos)
                currPage.postValue(pos)
                if (list.isEmpty()) {
                    LoadResult.Error(NotFoundException())
                } else {
                    LoadResult.Page(
                        data = list,
                        prevKey = if (pos == data.pageAndQuery.page) null else pos - 1,
                        nextKey = if (list.isEmpty()) null else pos + 1
                    )
                }

            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }
}