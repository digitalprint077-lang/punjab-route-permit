package com.pakexciseinfo.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun responsiveContentPadding(): Dp {
    val width = LocalConfiguration.current.screenWidthDp
    return when {
        width >= 840 -> 40.dp
        width >= 600 -> 28.dp
        else -> 20.dp
    }
}

@Composable
fun responsiveGridColumns(compact: Int = 2, medium: Int = 3, expanded: Int = 4): Int {
    val width = LocalConfiguration.current.screenWidthDp
    return when {
        width >= 840 -> expanded
        width >= 600 -> medium
        else -> compact
    }
}

/** Fade + rise. Prefer [enterFade] inside horizontally clipped lists (LazyRow). */
@Composable
fun Modifier.enterFadeUp(
    visible: Boolean = true,
    delayMs: Int = 0,
    durationMs: Int = 480,
): Modifier {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(visible) {
        if (visible) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = durationMs,
                    delayMillis = delayMs,
                    easing = FastOutSlowInEasing,
                ),
            )
        }
    }
    val value = progress.value
    return this
        .alpha(value)
        .offset { IntOffset(0, ((1f - value) * 18f).roundToInt()) }
}

/** Fade-only entrance — safe for LazyRow (no clip from vertical offset). */
@Composable
fun Modifier.enterFade(
    visible: Boolean = true,
    delayMs: Int = 0,
    durationMs: Int = 420,
): Modifier {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(visible) {
        if (visible) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = durationMs,
                    delayMillis = delayMs,
                    easing = FastOutSlowInEasing,
                ),
            )
        }
    }
    return this.alpha(progress.value)
}

@Composable
fun Modifier.pressScale(pressed: Boolean): Modifier {
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "pressScale",
    )
    return this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

@Composable
fun Modifier.navItemScale(selected: Boolean): Modifier {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.08f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "navScale",
    )
    return this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}
