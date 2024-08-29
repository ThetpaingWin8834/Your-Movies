package com.ym.yourmovies.utils.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.R
import com.ym.yourmovies.utils.components.ads.ApplovinBanner

@Composable
fun CategoriesCompose(
    modifier: Modifier = Modifier,
    list: List<CategoryItem>,
    onCateClick: (CategoryItem) -> Unit
) {
    Column(modifier=modifier.background(MaterialTheme.colorScheme.background)) {
        DescriptionTitle(title = stringResource(id = R.string.categories))
        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)){
            items(list){category->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onCateClick(category)
                        }
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = category.title,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyLarge,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(text = category.count)
                }

            }
        }
        ApplovinBanner()
    }

}