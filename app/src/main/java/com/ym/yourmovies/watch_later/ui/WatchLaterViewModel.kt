package com.ym.yourmovies.watch_later.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ym.yourmovies.utils.models.Channel
import com.ym.yourmovies.utils.others.MyConst
import com.ym.yourmovies.watch_later.data.WatchLaterDbProvider
import com.ym.yourmovies.watch_later.model.WatchLaterItem
import com.ym.yourmovies.watch_later.model.WlEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WatchLaterViewModel(
    private val app: Application
) : AndroidViewModel(application = app) {

    val watchLaterList = MutableStateFlow(emptyList<WatchLaterItem>())

    val isSelectedMode = MutableStateFlow(false)
    var selectedCount = combine(isSelectedMode, watchLaterList) { isSelected, list ->
        if (isSelected) {
            list.filter { it.isSelected }.size
        } else {
            0
        }
    }

    var isLoading by mutableStateOf(false)
        private set

    var query by mutableStateOf("")
        private set

    fun onQueryChange(char: String) {
        query = char
    }

    private var isSelectAll = false

    fun onSelectAll() {
        isSelectAll = if (isSelectAll) {
            watchLaterList.update { list ->
                list.map { it.copy(isSelected = false) }
            }
            false

        } else {
            watchLaterList.update { list ->
                list.map { it.copy(isSelected = true) }
            }
            true
        }

    }

    fun onDelete() {
        viewModelScope.launch(Dispatchers.IO) {
            for (item in watchLaterList.value) {
                if (item.isSelected) {
                    dao.delete(
                        WlEntity(
                            title = item.title,
                            id = item.id,
                            date = item.date,
                            rating = item.rating,
                            thumb = item.thumb,
                            url = item.url
                        )
                    )
                }
            }
            getWatchLaterMovieList()
            isSelectedMode.update { false }
        }
    }

    fun toggleSelectedMode() {
        isSelectedMode.value = !isSelectedMode.value
        if (!isSelectedMode.value) {
            watchLaterList.update {
                it.map { item ->
                    item.copy(isSelected = false)
                }
            }
            isSelectAll = false

        }
    }

    fun onSelect(item: WatchLaterItem) {
        watchLaterList.value = watchLaterList.value.map {
            if (it == item) {
                it.copy(isSelected = !item.isSelected)
            } else {
                it
            }
        }

    }

    private val dao by lazy {
        WatchLaterDbProvider.getWatchLaterDb(context = app.applicationContext).getWlDao()
    }

    fun getWatchLaterMovieList() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            watchLaterList.value = dao.getAllWatchLaterMovies()
                .map {
                    WatchLaterItem(
                        id = it.id,
                        title = it.title,
                        date = it.date,
                        rating = it.rating,
                        thumb = it.thumb,
                        url = it.url,
                        channel = when {
                            it.url.startsWith(MyConst.MSubHost) -> {
                                Channel.MyanmarSubMovie
                            }
                            it.url.startsWith(MyConst.GcHost) -> {
                                Channel.GoldChannel
                            }
                            else -> {
                                Channel.ChannelMyanmar
                            }
                        }
                    )
                }
            isLoading = false
        }
    }
}