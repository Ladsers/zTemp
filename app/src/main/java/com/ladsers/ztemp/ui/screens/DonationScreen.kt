package com.ladsers.ztemp.ui.screens

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.rememberScalingLazyListState
import com.ladsers.ztemp.R
import com.ladsers.ztemp.ui.components.ItemCard
import com.ladsers.ztemp.ui.components.TextInputCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DonationScreen(
    sendResult: (String) -> Unit,
    goToWebDonation: () -> Unit
) {
    val listState = rememberScalingLazyListState()

    Scaffold(
        vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
    ) {

        val focusRequester = remember { FocusRequester() }
        val coroutineScope = rememberCoroutineScope()

        val code = remember { mutableStateOf("") }

        ScalingLazyColumn(
            modifier = Modifier
                .onRotaryScrollEvent {
                    coroutineScope.launch {
                        listState.scrollBy(it.verticalScrollPixels)
                        listState.animateScrollBy(0f)
                    }
                    true
                }
                .focusRequester(focusRequester)
                .focusable(),
            state = listState
        ) {
            item {
                ListHeader(modifier = Modifier.padding(bottom = 5.dp)) {
                    Text(
                        text = stringResource(id = R.string.title_activity_donation),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                }
            }
            item {
                Text(
                    text = stringResource(id = R.string.supportDeveloperDescription),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 15.dp, start = 6.dp, end = 6.dp)
                )
            }
            item {
                ItemCard(
                    title = stringResource(id = R.string.goToDonation),
                    enabled = true,
                    onClickAction = goToWebDonation
                )
            }
            item {
                TextInputCard(
                    label = stringResource(id = R.string.code),
                    valueState = code,
                    onUpdateValue = { c -> code.value = c }
                )
            }
            item {
                Button(
                    modifier = Modifier.padding(top = 15.dp),
                    onClick = { sendResult(code.value) }) {
                    Icon(
                        imageVector = Icons.Rounded.Done,
                        contentDescription = stringResource(id = R.string.cd_confirm)
                    )
                }
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()

            launch {
                listState.scrollToItem(0, 0)
            }
        }
    }
}

