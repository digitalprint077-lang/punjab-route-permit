package com.pakexciseinfo.app.ui.more

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Refresh
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pakexciseinfo.app.BuildConfig
import com.pakexciseinfo.app.R
import com.pakexciseinfo.app.data.AppContent
import com.pakexciseinfo.app.ui.components.enterFadeUp
import com.pakexciseinfo.app.ui.components.pressScale
import com.pakexciseinfo.app.ui.components.responsiveContentPadding
import com.pakexciseinfo.app.ui.theme.Fog
import com.pakexciseinfo.app.ui.theme.Ink
import com.pakexciseinfo.app.ui.theme.Sand
import com.pakexciseinfo.app.ui.theme.Sea
import com.pakexciseinfo.app.ui.theme.SeaDeep

@Composable
fun MoreScreen(
    onOpenUrl: (String) -> Unit,
    onRefreshConfig: () -> Unit,
) {
    val pad = responsiveContentPadding()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
    ) {
        item {
            BoxProfile(pad = pad)
        }
        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = pad)
                    .padding(top = 22.dp)
                    .enterFadeUp(delayMs = 70),
            ) {
                Text(
                    text = stringResource(id = R.string.more_group_tools),
                    style = MaterialTheme.typography.labelLarge,
                    color = SeaDeep,
                )
                Spacer(modifier = Modifier.height(10.dp))
                MoreRow(
                    icon = Icons.Rounded.Refresh,
                    title = stringResource(id = R.string.refresh_links),
                    subtitle = stringResource(id = R.string.refresh_links_hint),
                    onClick = onRefreshConfig,
                )
            }
        }
        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = pad)
                    .padding(top = 22.dp)
                    .enterFadeUp(delayMs = 110),
            ) {
                Text(
                    text = stringResource(id = R.string.more_group_info),
                    style = MaterialTheme.typography.labelLarge,
                    color = SeaDeep,
                )
                Spacer(modifier = Modifier.height(10.dp))
                MoreRow(
                    icon = Icons.Rounded.Info,
                    title = stringResource(id = R.string.about),
                    subtitle = stringResource(id = R.string.about_hint),
                    onClick = { onOpenUrl(AppContent.siteUrl("about.html")) },
                )
                MoreRow(
                    icon = Icons.Rounded.MailOutline,
                    title = stringResource(id = R.string.contact),
                    subtitle = stringResource(id = R.string.contact_hint),
                    onClick = { onOpenUrl(AppContent.siteUrl("contact.html")) },
                )
                MoreRow(
                    icon = Icons.Rounded.Policy,
                    title = stringResource(id = R.string.privacy),
                    subtitle = stringResource(id = R.string.privacy_hint),
                    onClick = { onOpenUrl(AppContent.siteUrl("privacy.html")) },
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.app_version, BuildConfig.VERSION_NAME),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun BoxProfile(pad: Dp) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(listOf(Color(0xFFCDEEDB), Sand, Fog)),
            )
            .padding(horizontal = pad, vertical = 28.dp)
            .enterFadeUp(),
        horizontalAlignment = Alignment.Start,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_brand),
            contentDescription = null,
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            color = Ink,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.more_profile_sub),
            style = MaterialTheme.typography.bodyMedium,
            color = SeaDeep,
        )
    }
}

@Composable
private fun MoreRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .pressScale(pressed)
            .clip(RoundedCornerShape(18.dp))
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Sand),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Sea,
                    modifier = Modifier.size(22.dp),
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = SeaDeep.copy(alpha = 0.45f),
            )
        }
    }
}
