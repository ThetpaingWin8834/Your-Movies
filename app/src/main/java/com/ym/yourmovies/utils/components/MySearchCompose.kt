package com.ym.yourmovies.utils.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ym.yourmovies.R
import com.ym.yourmovies.ui.search.ui.SearchAppBar
import com.ym.yourmovies.utils.models.NameAndUrlModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
inline fun <reified Movie:Any>MySearchCompose(
    recentList: List<String>,
    noinline onRecentClick: (String) -> Unit,
    cateList: List<NameAndUrlModel>,
    noinline onCateClick: (NameAndUrlModel) -> Unit,
    randomMovieList: List<Movie>,
    noinline onMovieClick: (Movie) -> Unit
) {
    Scaffold(
        topBar = {
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)

        ) {
            Column {
                if (randomMovieList.isNotEmpty()){
                    //////Random movies
                    MyRelated(list = randomMovieList, onItemClick = onMovieClick, title = stringResource(
                        id = R.string.random_movies
                    ))
                }


            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun Search(
) {
//    SearchAppBar()
}