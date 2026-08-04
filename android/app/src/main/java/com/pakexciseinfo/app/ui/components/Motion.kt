package com.pakexciseinfo.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
        .offset { IntOffset(0, ((1f - value) * 22f).roundToInt()) }
}

@Composable
fun Modifier.pressScale(pressed: Boolean): Modifier {
    val scale = remember { Animatable(1f) }
    LaunchedEffect(pressed) {
        scale.animateTo(
            targetValue = if (pressed) 0.96f else 1f,
            animationSpec = tween(durationMillis = 110, easing = FastOutSlowInEasing),
        )
    }
    return this.graphicsLayer {
        scaleX = scale.value
        scaleY = scale.value
    }
}
