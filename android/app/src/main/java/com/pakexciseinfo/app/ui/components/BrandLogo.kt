package com.pakexciseinfo.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pakexciseinfo.app.R

/**
 * Hub-wheel brand mark with a continuous tyre-style rotation.
 * @param durationMs one full revolution (slower = more tyre-like roll)
 */
@Composable
fun SpinningBrandLogo(
    modifier: Modifier = Modifier,
    size: Dp = 88.dp,
    durationMs: Int = 4200,
    elevated: Boolean = true,
    contentDescription: String? = stringResource(id = R.string.app_name),
) {
    val transition = rememberInfiniteTransition(label = "tyreSpin")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "tyreRotation",
    )

    Image(
        painter = painterResource(id = R.drawable.ic_brand),
        contentDescription = contentDescription,
        modifier = modifier
            .size(size)
            .then(
                if (elevated) {
                    Modifier.shadow(14.dp, CircleShape, clip = false)
                } else {
                    Modifier
                },
            )
            .clip(CircleShape)
            .graphicsLayer {
                rotationZ = rotation
            },
        contentScale = ContentScale.Fit,
    )
}
