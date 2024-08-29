package com.ym.yourmovies.ui.search.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ym.yourmovies.R
import com.ym.yourmovies.ui.search.result.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun SearchAppBar(
    viewModel: SearchViewModel,
    focusRequester: FocusRequester,
    scrollBehavior: TopAppBarScrollBehavior,
    onSearch: () -> Unit,
    onBack: () -> Unit
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }
    TopAppBar(
        title = {

        },
        scrollBehavior = scrollBehavior,
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back")
                }
                BasicTextField(
                    value = viewModel.query,
                    onValueChange =viewModel::onQueryChange,
                    modifier = Modifier
                        .height(40.dp)
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onFocusChanged(onFocusChanged = viewModel::onFocusChange),
                    singleLine = true,
                    interactionSource = interactionSource,
                    cursorBrush = SolidColor(LocalContentColor.current),
                    textStyle = TextStyle.Default.copy(
                        color = LocalContentColor.current
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
                        onSearch()
                    })
                ) {
                    TextFieldDefaults.TextFieldDecorationBox(
                        value = viewModel.query,
                        innerTextField = it,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        placeholder = { Text(text = stringResource(R.string.search_hint)) },
                        interactionSource = interactionSource,
                        leadingIcon = {
                            IconButton(onClick = onSearch) {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = "Search"
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.onQueryChange("")}) {
                                Icon(
                                    imageVector = Icons.Rounded.Clear,
                                    contentDescription = "Clear"
                                )
                            }
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                            top = 0.dp,
                            bottom = 0.dp
                        ),

                        )
                }
                TextButton(onClick = onSearch) {
                    Text(text = stringResource(R.string.search))
                }
            }


        }
    )
}