package com.sidukov.sparvel.features.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.Route
import com.sidukov.sparvel.core.theme.SparvelTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    navController: NavHostController
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val cls = listOf(
            SparvelTheme.colors.searchText, SparvelTheme.colors.text
        )
        var visible by remember { mutableStateOf(false) }
        val density = LocalDensity.current
        val duration = 2000
        val gradient = SparvelTheme.colors.logoGradient

        AnimatedVisibility(
            visible,
            enter = slideInVertically(animationSpec = tween(durationMillis = duration)) {
                with(density) { -30.dp.roundToPx() }
            } + fadeIn(animationSpec = tween(durationMillis = duration))
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
            visible,
            enter = slideInVertically(animationSpec = tween(durationMillis = duration)) {
                with(density) { 30.dp.roundToPx() }
            } + fadeIn(animationSpec = tween(durationMillis = duration))) {
            Text(
                text = stringResource(R.string.app_name),
                style = SparvelTheme.typography.appName,
                color = SparvelTheme.colors.text
            )
        }

        // look up which type of side effect I should use
        LaunchedEffect(true) {
            visible = true
            delay(3000)
            navController.navigate(Route.DRAWER_CONTAINER)
        }
    }
}