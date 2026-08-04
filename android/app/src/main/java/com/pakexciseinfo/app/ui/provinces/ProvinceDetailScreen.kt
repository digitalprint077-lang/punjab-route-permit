package com.pakexciseinfo.app.ui.provinces

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pakexciseinfo.app.R
import com.pakexciseinfo.app.data.AppContent
import com.pakexciseinfo.app.data.Province
import com.pakexciseinfo.app.ui.components.CategoryCard
import com.pakexciseinfo.app.ui.components.GhostButton
import com.pakexciseinfo.app.ui.components.PrimaryButton
import com.pakexciseinfo.app.ui.components.ScreenHeader
import com.pakexciseinfo.app.ui.components.SectionHeader
import com.pakexciseinfo.app.ui.components.enterFadeUp
import com.pakexciseinfo.app.ui.components.responsiveContentPadding
import com.pakexciseinfo.app.ui.theme.Fog
import com.pakexciseinfo.app.ui.theme.Ink
import com.pakexciseinfo.app.ui.theme.Sand
import com.pakexciseinfo.app.ui.theme.Sea

@Composable
fun ProvinceDetailScreen(
    province: Province?,
    onBack: () -> Unit,
    onOpenOfficial: (String) -> Unit,
    onOpenGuide: (String) -> Unit,
) {
    val pad = responsiveContentPadding()
    if (province == null) {
        Column(modifier = Modifier.fillMaxSize().padding(pad)) {
            ScreenHeader(title = stringResource(id = R.string.not_found), onBack = onBack)
            PrimaryButton(text = stringResource(id = R.string.go_back), onClick = onBack)
        }
        return
    }

    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(bottom = 32.dp),
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(listOf(Color(0xFFCDEEDB), Sand, Fog)),
                    )
                    .padding(horizontal = pad)
                    .padding(bottom = 22.dp)
                    .enterFadeUp(),
            ) {
                Column {
                    ScreenHeader(title = province.name, onBack = onBack)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = province.logoRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(76.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White)
                                .padding(10.dp),
                            contentScale = ContentScale.Fit,
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Column {
                            Text(
                                text = province.badge.uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                                color = Sea,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = province.name,
                                style = MaterialTheme.typography.headlineSmall,
                                color = Ink,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = province.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Ink.copy(alpha = 0.8f),
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        PrimaryButton(
                            text = stringResource(id = R.string.visit_portal),
                            onClick = { onOpenOfficial(province.portalUrl) },
                        )
                        GhostButton(
                            text = stringResource(id = R.string.read_guide),
                            onClick = { onOpenGuide(AppContent.siteUrl(province.guidePath)) },
                        )
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = pad)
                    .padding(top = 22.dp, bottom = 10.dp)
                    .enterFadeUp(delayMs = 80),
            ) {
                SectionHeader(
                    title = stringResource(id = R.string.section_service_categories),
                    subtitle = stringResource(id = R.string.section_service_categories_sub),
                )
            }
        }

        itemsIndexed(province.services, key = { _, s -> s.id }) { index, service ->
            CategoryCard(
                title = service.title,
                description = service.description,
                iconRes = service.iconRes,
                onClick = { onOpenOfficial(service.officialUrl) },
                actionHint = stringResource(id = R.string.open_service),
                modifier = Modifier
                    .padding(horizontal = pad, vertical = 6.dp)
                    .enterFadeUp(delayMs = 40 + index * 35),
            )
        }
    }
}
