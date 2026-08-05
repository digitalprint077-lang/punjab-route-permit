package com.vehiclehubpk.app.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vehiclehubpk.app.ui.theme.Gold
import com.vehiclehubpk.app.ui.theme.Sand
import com.vehiclehubpk.app.ui.theme.Sea
import com.vehiclehubpk.app.ui.theme.SeaDeep

private val CardShape = RoundedCornerShape(22.dp)

@Composable
fun ProvinceCard(
    name: String,
    badge: String,
    description: String,
    @DrawableRes logoRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = if (compact) 168.dp else 0.dp)
            .pressScale(pressed)
            .clip(CardShape)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        shape = CardShape,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(
                        brush = Brush.horizontalGradient(listOf(Sea, SeaDeep.copy(alpha = 0.7f))),
                    ),
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .then(if (compact) Modifier.fillMaxSize() else Modifier),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = logoRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Sand)
                            .padding(8.dp),
                        contentScale = ContentScale.Fit,
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = badge.uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            color = Sea,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        tint = SeaDeep.copy(alpha = 0.45f),
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = if (compact) 2 else 3,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true,
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    title: String,
    description: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    actionHint: String = "Open service",
    secondaryAction: String? = null,
    onSecondaryAction: (() -> Unit)? = null,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .pressScale(pressed)
            .clip(CardShape)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        shape = CardShape,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Sand),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentScale = ContentScale.Fit,
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = actionHint,
                        style = MaterialTheme.typography.labelLarge,
                        color = Sea,
                    )
                    if (secondaryAction != null && onSecondaryAction != null) {
                        Text(
                            text = secondaryAction,
                            style = MaterialTheme.typography.labelLarge,
                            color = SeaDeep,
                            modifier = Modifier.clickable(onClick = onSecondaryAction),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CompactCategoryTile(
    title: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    Surface(
        modifier = modifier
            .pressScale(pressed)
            .clip(CardShape)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        shape = CardShape,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Sand),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(34.dp)
                        .padding(2.dp),
                    contentScale = ContentScale.Fit,
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Gold),
            )
        }
    }
}
