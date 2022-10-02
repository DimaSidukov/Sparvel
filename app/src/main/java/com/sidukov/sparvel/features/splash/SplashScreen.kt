package com.sidukov.sparvel.features.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.Screens
import com.sidukov.sparvel.core.functionality.navigateAndSetRoot
import com.sidukov.sparvel.core.functionality.systemBarsPadding
import com.sidukov.sparvel.core.functionality.toJsonString
import com.sidukov.sparvel.core.theme.SparvelTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    navController: NavHostController
) {

    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val density = LocalDensity.current
        val duration = 600
        val gradient = SparvelTheme.colors.logoGradient

        AnimatedVisibility(
            visible = uiState.isAnimationVisible,
            enter = slideInVertically(tween(duration)) {
                with(density) { -30.dp.roundToPx() }
            } + fadeIn(tween(duration / 3))
        ) {
            Icon(
                modifier = Modifier
                    .size(60.dp)
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient, blendMode = BlendMode.SrcAtop)
                        }
                    },
                painter = painterResource(R.drawable.ic_sparvel_logo), contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        AnimatedVisibility(
            visible = uiState.isAnimationVisible,
            enter = slideInVertically(tween(duration)) {
                with(density) { 30.dp.roundToPx() }
            } + fadeIn(tween(duration / 3))) {
            Text(
                text = stringResource(R.string.app_name),
                style = SparvelTheme.typography.appName,
                color = SparvelTheme.colors.text
            )
        }
        LaunchedEffect(uiState.isDataLoaded) {
            viewModel.setAnimationVisible()
            if (uiState.isDataLoaded) {
                delay(duration.toLong())
                launch(Dispatchers.Main) {
                    navController.navigateAndSetRoot(Screens.Home.passTrackList(uiState.trackList.toJsonString()))
                }
            }
            launch {
                viewModel.readTracks()
            }
        }
    }
}