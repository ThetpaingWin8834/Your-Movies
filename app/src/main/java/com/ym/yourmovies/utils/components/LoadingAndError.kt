package com.ym.yourmovies.utils.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ym.yourmovies.utils.custom.MyError

@Composable
fun LoadingAndError(
    isLoading: Boolean,
    error: Throwable?,
    onErrorClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    if (isLoading) {
        CircularProgressIndicator(modifier = modifier)
    }
    if (error != null) {
        MyError(exception = error, onBtClick = onErrorClick)
    }
//    if (error != null) {
//        val painter: Painter
//        val shortTitle: String
//        when (error) {
//            is HttpStatusException, is UnknownHostException -> {
//                shortTitle = stringResource(R.string.try_vpn)
//                painter = painterResource(id = R.drawable.ic_no_conn)
//            }
//            is NotFoundException -> {
//                shortTitle = stringResource(R.string.not_found)
//                painter = painterResource(id = R.drawable.ic_not_found)
//            }
//            else -> {
//                shortTitle = stringResource(R.string.unknown_error)
//                painter = painterResource(id = R.drawable.ic_sad)
//
//            }
//        }
//        var isShowDetailError by remember {
//            mutableStateOf(false)
//        }
//        Column(
//            modifier = modifier,
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Icon(painter = painter, contentDescription = null, modifier = Modifier.size(100.dp))
//            Spacer(modifier = Modifier.width(5.dp))
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(5.dp),
//            ) {
//                Text(text = shortTitle, style = MaterialTheme.typography.titleMedium, maxLines = 1)
//                IconButton(onClick = { isShowDetailError = !isShowDetailError }) {
//                    Icon(
//                        imageVector = Icons.Rounded.ArrowDropDown,
//                        contentDescription = null,
//                        modifier = Modifier.size(50.dp).rotate(if (isShowDetailError) 0f else 180f)
//                    )
//                }
//            }
//            if (isShowDetailError) {
//                Text(
//                    text = error.localizedMessage ?: stringResource(id = R.string.unknown_error),
//                    style = MaterialTheme.typography.bodySmall,
//                    color = LocalContentColor.current.copy(alpha = 0.5f),
//                    modifier = Modifier.padding(7.dp)
//                )
//            }
//            MyButton(
//                text = stringResource(id = R.string.retry),
//                modifier = Modifier.fillMaxWidth(),
//                onClick = onErrorClick
//            )
//        }
//    }


}