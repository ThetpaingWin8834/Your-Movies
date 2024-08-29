package com.ym.yourmovies.ui.settings

import android.content.Context
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ym.yourmovies.R
import com.ym.yourmovies.ui.theme.YourMoviesTheme
import com.ym.yourmovies.utils.components.MyDivider


@Composable
fun SettingsScreen(
    onUpdate: () -> Unit,
    onback:()->Unit
) {
    val viewModel: SettingsViewModel = viewModel()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        UserInterface(onUpdate = onUpdate, viewModel = viewModel)
    }
    BackHandler(onBack = onback)
}

@Composable
fun UserInterface(
    context: Context = LocalContext.current,
    viewModel: SettingsViewModel,
    onUpdate: () -> Unit,
) {
    val currentTheme = viewModel.currentTheme
    val currentLang = viewModel.currentLang
    val defChannel = viewModel.defaultChannel
    val currIsLinear = viewModel.currentIsLinear
    val useDynamic = viewModel.currUseDynamic

    val currGridCount = viewModel.currentGridCount


    val currDownloadMethod = viewModel.defDm

    SettingsDesc(text = stringResource(id = R.string.user_interface))
    ListPrefCompose(
        mainText = stringResource(id = R.string.theme),
        currentSelected = currentTheme, list = viewModel.listOfTheme,
        onItemSelected = viewModel::setCurrTheme,
        icon = {
            Icon(painter = painterResource(id = R.drawable.ic_palette), contentDescription = null)
        })
    if (Build.VERSION.SDK_INT >= 31) {
        SwitchPrefCompose(
            mainText = stringResource(id = R.string.use_dynamic_color),
            isChecked = useDynamic,
            onCheckChange = viewModel::changeUseDynamicTo
        )
    }
    ListPrefCompose(
        mainText = stringResource(id = R.string.language),
        currentSelected = currentLang, list = viewModel.listOfLang,
        onItemSelected = viewModel::setCurrLang,
        icon = {
            Icon(painter = painterResource(id = R.drawable.ic_network), contentDescription = null)
        })

    SwitchPrefCompose(
        mainText = stringResource(id = R.string.align_linear),
        isChecked = currIsLinear,
        onCheckChange = viewModel::changeIsLinearTo,
        icon = {
            Icon(painterResource(id = R.drawable.ic_linear), contentDescription = null)
        }
    )
    if (!currIsLinear) {
        ListPrefCompose(
            mainText = stringResource(id = R.string.grid_count),
            currentSelected = currGridCount.toString(),
            list = viewModel.listOfGirdCount,
            onItemSelected = {
                viewModel.changeGridCount(it.toInt())
            },
            icon = {
                Icon(painterResource(id = R.drawable.ic_grid), contentDescription = null)
            }
        )
    }
    SettingsDesc(text = stringResource(id = R.string.channel_pref))
    ListPrefCompose(
        mainText = stringResource(id = R.string.default_channel),
        currentSelected = defChannel.name,
        list = viewModel.listOfChannel,
        onItemSelected = viewModel::changeDefaultChannel,
        icon = {
            Icon(painter = painterResource(id = R.drawable.ic_movie), contentDescription = null)
        }
    )
    val currSearchIn = viewModel.currSearchIn
    ListPrefCompose(
        mainText = stringResource(id = R.string.search_movies_in),
        currentSelected = currSearchIn,
        list = viewModel.listOfSearchAndRecomm,
        onItemSelected = viewModel::setCurrSearchChannel,
        icon = {
            Icon(painter = painterResource(id = R.drawable.ic_search), contentDescription = null)
        }
    )
    val currRecommIn = viewModel.currRecommIn
    ListPrefCompose(
        mainText = stringResource(id = R.string.recomm_from),
        currentSelected = currRecommIn,
        list = viewModel.listOfSearchAndRecomm+ NONE,
        onItemSelected = viewModel::setCurrRecommChannel
    )



    SettingsDesc(text = stringResource(id = R.string.download))
    ListPrefCompose(
        mainText = stringResource(id = R.string.always_use_dm),
        currentSelected = currDownloadMethod,
        list = viewModel.listOfDm,
        onItemSelected = viewModel::onDownloadmanagerChange,
        icon = {
            Icon(painter = painterResource(id = R.drawable.ic_movie), contentDescription = null)
        }
    )

    LaunchedEffect(key1 = currentLang) {
        val lang = viewModel.getDefaultLanguage()
        if (lang != currentLang) {
            MySettingsManager.setLang(context, currentLang)
            onUpdate()
        }
    }
    LaunchedEffect(key1 = currentTheme) {
        val theme = viewModel.getDefaultTheme()
        if (theme != currentTheme) {
            MySettingsManager.setTheme(context, currentTheme)
            onUpdate()
        }
    }
    LaunchedEffect(key1 = currIsLinear) {
        val isLinear = viewModel.getIsLnear()
        if (isLinear != currIsLinear) {
            MySettingsManager.setIsLinearTo(context, currIsLinear)
            onUpdate()
        }
    }
    LaunchedEffect(key1 = useDynamic) {
        val dynmaic = viewModel.getUseDynamic()
        if (useDynamic != dynmaic) {
            MySettingsManager.setUseDynamic(context, useDynamic)
        }
    }
    LaunchedEffect(key1 = currGridCount) {
        val gridCount = viewModel.getDefaultGridCount()
        if (gridCount != currGridCount) {
            MySettingsManager.setGridCount(context, currGridCount)
            onUpdate()
        }
    }
    LaunchedEffect(key1 = currDownloadMethod) {
        val dm = viewModel.getDefaultDownloadManager()
        if (dm != currDownloadMethod) {
            MySettingsManager.setDownloadWith(context, currDownloadMethod)
        }
    }
    LaunchedEffect(key1 = currSearchIn) {
        val searchIn = viewModel.getDefSearchInChannel()
        if (searchIn != currSearchIn) {
            MySettingsManager.setDefaultSearchIn(context, currSearchIn)
        }
    }
    LaunchedEffect(key1 = currRecommIn) {
        val recommIn = viewModel.getDefRecommChannel()
        if (recommIn != currRecommIn) {
            MySettingsManager.setDefaultRecomm(context, currRecommIn)
        }
    }

}

@Composable
private fun SwitchPrefCompose(
    mainText: String,
    isChecked: Boolean,
    icon: (@Composable () -> Unit)? = null,
    onCheckChange: (Boolean) -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Spacer(modifier = Modifier.width(7.dp))
                icon()
            }
            Text(
                text = mainText, modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            )
            Switch(checked = isChecked, onCheckedChange = onCheckChange)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListPrefCompose(
    mainText: String,
    currentSelected: String,
    list: List<String>,
    icon: (@Composable () -> Unit)? = null,
    onItemSelected: (String) -> Unit,
) {
    var isShowDia by remember {
        mutableStateOf(false)
    }
    val titlePadding = remember {
        PaddingValues(start = if (icon==null) (28+8).dp else 8.dp , top = 8.dp, bottom = 8.dp)
    }
    val subTextPadding = remember {
        PaddingValues(start = if (icon==null) (28+8+5).dp else (8+5).dp, bottom = 8.dp)
    }

    ElevatedCard(
        onClick = {
            isShowDia = true
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Spacer(modifier = Modifier.width(7.dp))
                icon()
            }
            Column {
                Text(text = mainText, modifier = Modifier.padding(titlePadding))
                Text(
                    text = currentSelected,
                    style = MaterialTheme.typography.labelSmall,
                    color = LocalContentColor.current.copy(alpha = 0.7f),
                    modifier = Modifier.padding(subTextPadding)
                )
            }
        }

    }
    if (isShowDia) {
        AlertDialog(onDismissRequest = { isShowDia = false },
            confirmButton = {},
            title = {
                Text(text = mainText)
            },
            text = {
                Column {
                    for (text in list) {
                        TextButton(
                            onClick = {
                                onItemSelected(text)
                                isShowDia = false
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(7.dp)

                        ) {
                            Text(
                                text = text, modifier = Modifier
                                    .weight(1f)
                                    .padding(10.dp),
                                style = MaterialTheme.typography.titleMedium
                            )
                            if (text == currentSelected) {
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                        }
                        MyDivider()

                    }
                }
            })
    }

}

@Composable
fun SettingsDesc(text: String) {
    Text(
        modifier = Modifier.padding(16.dp),
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary
    )
}