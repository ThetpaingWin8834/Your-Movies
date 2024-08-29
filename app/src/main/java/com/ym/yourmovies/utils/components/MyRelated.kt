package com.ym.yourmovies.utils.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.ym.yourmovies.R
import com.ym.yourmovies.cm.details.ui.CmMovieNoConstriant
import com.ym.yourmovies.cm.home.models.CmMovie
import com.ym.yourmovies.gc.home.models.GcMovie
import com.ym.yourmovies.gc.compose.GcItemGrid
import com.ym.yourmovies.msub.model.MSubMovie
import com.ym.yourmovies.msub.compose.MSubMovieGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun <reified Movie : Any> MyRelated(
    title:String = stringResource(id = R.string.related),
    list: List<Movie>,
    noinline onItemClick: (Movie) -> Unit
) {
    DescriptionTitle(title = title)
    when (Movie::class.java) {
        CmMovie::class.java -> {
            FlowRow {
                for (movie in list) {
                    CmMovieNoConstriant(
                        item = movie as CmMovie,
                        onclick = {
                            onItemClick(movie)
                        }
                    )
                }
            }
        }
        GcMovie::class.java -> {
            FlowRow(modifier = Modifier.fillMaxWidth()) {
                for (movie in list) {
                    GcItemGrid(
                        movie = movie as GcMovie,
                        onGcClick = { onItemClick(movie) },
                        modifier = Modifier
                            .fillMaxWidth(0.33f)
                            .padding(3.dp)
                    )
                }
            }
        }
        MSubMovie::class.java -> {
            FlowRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                for (movie in list) {
                    MSubMovieGrid(
                        movie = movie as MSubMovie,
                        onMovieClick = { onItemClick(movie) },
                        modifier = Modifier
                            .fillMaxWidth(0.33f)
                            .padding(3.dp)
                    )
                }
            }
        }
    }
}