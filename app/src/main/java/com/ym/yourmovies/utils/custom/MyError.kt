package com.ym.yourmovies.utils.custom

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ym.yourmovies.R
import com.ym.yourmovies.utils.components.MyButton
import com.ym.yourmovies.utils.others.NotFoundException
import org.jsoup.HttpStatusException
import java.net.UnknownHostException

@Composable
fun MyError(
    modifier: Modifier = Modifier,
    exception: Throwable,
    onBtClick: () -> Unit,
) {
    val painter: Painter
    val shortTitle: String
    when (exception) {
        is HttpStatusException, is UnknownHostException -> {
            shortTitle = stringResource(R.string.try_vpn)
            painter = painterResource(id = R.drawable.ic_no_conn)
        }
        is NotFoundException -> {
            shortTitle = stringResource(R.string.not_found)
            painter = painterResource(id = R.drawable.ic_not_found)
        }
        else -> {
            shortTitle = stringResource(R.string.unknown_error)
            painter = painterResource(id = R.drawable.ic_sad)

        }
    }
    var isShowDetailError by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .animateContentSize()
            .padding(horizontal = 7.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painter,
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = shortTitle,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2
            )
            IconButton(onClick = { isShowDetailError = !isShowDetailError }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(80.dp)
                        .rotate(if (isShowDetailError) 180f else 0f)
                )
            }
        }
        if (isShowDetailError) {
            Text(
                text = exception.localizedMessage ?: stringResource(id = R.string.unknown_error),
                style = MaterialTheme.typography.bodySmall,
                color =MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                modifier = Modifier.padding(7.dp)
            )
        }
        MyButton(
            text = stringResource(id = R.string.retry),
            modifier = Modifier.fillMaxWidth(),
            onClick = onBtClick
        )
    }
}