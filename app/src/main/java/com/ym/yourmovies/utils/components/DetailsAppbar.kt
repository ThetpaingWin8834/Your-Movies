package com.ym.yourmovies.utils.components

import android.content.Context
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.ym.yourmovies.R
import com.ym.yourmovies.utils.others.Goto
import com.ym.yourmovies.utils.others.Ym
import com.ym.yourmovies.watch_later.data.WatchLaterDbProvider
import com.ym.yourmovies.watch_later.model.WlEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsAppbar(
    title: String,
    context: Context = LocalContext.current,
    movie: WlEntity,
    snackbarHostState: SnackbarHostState,
    onBackPressedDispatcher: OnBackPressedDispatcher,
    scrollBehavior: TopAppBarScrollBehavior
) {

    val dao = remember {
        WatchLaterDbProvider.getWatchLaterDb(context).getWlDao()
    }
    val scope = rememberCoroutineScope()
    val savedData = dao.getWlIfExist(movie.url).collectAsState(null).value
    val drawableId by remember(key1 = savedData) {
        derivedStateOf {
            if (savedData == null) {
                R.drawable.ic_save
            } else {
                R.drawable.ic_saved
            }
        }
    }
    var openDropdownMenu by remember {
        mutableStateOf(false)
    }
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        navigationIcon = {
            IconButton(onClick = onBackPressedDispatcher::onBackPressed) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = {
                scope.launch(Dispatchers.IO) {
                    if (savedData == null) {
                        dao.insertWatchLater(
                            movie
                        )
                        snackbarHostState.showSnackbar(context.getString(R.string.added_to_wl))

                    } else {
                        dao.delete(wlEntity = savedData)
                        snackbarHostState.showSnackbar(context.getString(R.string.removed_from_wl))


                    }
                }
            }) {
                Icon(
                    painter = painterResource(id = drawableId),
                    contentDescription = null
                )
            }
            IconButton(onClick = {
                Goto.goSearch(context)
            }) {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
            }
            IconButton(onClick = { openDropdownMenu = true }) {
                Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
            }
            DropdownMenu(
                expanded = openDropdownMenu,
                onDismissRequest = { openDropdownMenu = false }) {
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.watch_later_list))
                    },
                    onClick = {
                        openDropdownMenu = false
                        Goto.goWatchLater(context)
                              },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_watch_later),
                            contentDescription = null
                        )
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.copy_link))
                    },
                    onClick = {
                        openDropdownMenu = false
                        Ym.copyLink(context = context, link = movie.url)
                              },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_copy_link),
                            contentDescription = null
                        )
                    }
                )
            }

        }
    )
}