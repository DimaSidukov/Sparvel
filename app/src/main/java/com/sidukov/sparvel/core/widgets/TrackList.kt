package com.sidukov.sparvel.core.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.SelectedTrackPadding
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.theme.SparvelTheme

@Composable
fun TrackList(
    modifier: Modifier = Modifier,
    itemList: List<Track>,
    selectedTrackId: String?,
    isAudioPlaying: Boolean,
    needShowSettings: Boolean,
    onItemClicked: (Track) -> Unit = { },
    additionalContent: @Composable () -> Unit = { }
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            additionalContent()
        }
        items(
            items = itemList,
            contentType = { it },
            key = {
                it.id
            }
        ) { item ->
            Box {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(modifier)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(
                                color = SparvelTheme.colors.cursor
                            )
                        ) { onItemClicked(item) },
                    horizontalArrangement = Arrangement.Start
                ) {
                    SparvelImage(
                        imageUri = item.coverId,
                        imageSize = 50,
                        needGradient = false
                    )
                    Column(
                        modifier = Modifier
                            .height(50.dp)
                            .padding(start = 10.dp),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            item.name,
                            modifier = Modifier.padding(end = 50.dp),
                            style = SparvelTheme.typography.trackNameSmall,
                            color = SparvelTheme.colors.secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = stringResource(
                                R.string.artist_album_label,
                                item.artist,
                                item.album
                            ),
                            modifier = Modifier.padding(end = 50.dp),
                            style = SparvelTheme.typography.collectionInfo,
                            color = SparvelTheme.colors.secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (selectedTrackId != null && item.id == selectedTrackId) {
                    val composition by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(
                            R.raw.anim_audiowave
                        )
                    )
                    LottieAnimation(
                        composition,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 30.dp)
                            .size(40.dp),
                        isPlaying = isAudioPlaying,
                        iterations = LottieConstants.IterateForever
                    )
                }
                if (needShowSettings) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 5.dp)
                            .size(40.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(
                                    radius = 20.dp
                                ),
                                onClick = {
                                    // on song settings clicked
                                    // show bottom sheet dialog with different functionality, such as
                                    // share song, edit song info, remove from device, add to playlist
                                }
                            )
                    ) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(30.dp),
                            painter = painterResource(id = R.drawable.ic_three_dots),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(SparvelTheme.colors.playerActions)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
        item {
            SelectedTrackPadding(
                isTrackSelected = selectedTrackId != null,
                defaultPadding = 15.dp
            )
        }
    }
}