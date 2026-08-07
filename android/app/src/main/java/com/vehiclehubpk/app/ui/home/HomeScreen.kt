package com.vehiclehubpk.app.ui.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vehiclehubpk.app.R
import com.vehiclehubpk.app.data.AppContent
import com.vehiclehubpk.app.data.GuideItem
import com.vehiclehubpk.app.data.PortalInfo
import com.vehiclehubpk.app.data.Province
import com.vehiclehubpk.app.data.UrlHelpers
import com.vehiclehubpk.app.data.guideOpenCta
import com.vehiclehubpk.app.ui.components.CompactCategoryTile
import com.vehiclehubpk.app.ui.components.GhostButton
import com.vehiclehubpk.app.ui.components.PrimaryButton
import com.vehiclehubpk.app.ui.components.AffiliationDisclaimer
import com.vehiclehubpk.app.ui.components.ProvinceCard
import com.vehiclehubpk.app.ui.components.RegionIcons
import com.vehiclehubpk.app.ui.components.SectionHeader
import com.vehiclehubpk.app.ui.components.SpinningBrandLogo
import com.vehiclehubpk.app.ui.components.enterFade
import com.vehiclehubpk.app.ui.components.enterFadeUp
import com.vehiclehubpk.app.ui.components.responsiveContentPadding
import com.vehiclehubpk.app.ui.components.responsiveGridColumns
import com.vehiclehubpk.app.ui.theme.Fog
import com.vehiclehubpk.app.ui.theme.Ink
import com.vehiclehubpk.app.ui.theme.Sand
import com.vehiclehubpk.app.ui.theme.Sea
import com.vehiclehubpk.app.ui.theme.SeaDeep

@Composable
fun HomeScreen(
    guides: List<GuideItem>,
    provinces: List<Province>,
    onProvinceClick: (String) -> Unit,
    onOpenProvinces: () -> Unit,
    onOpenServices: () -> Unit,
    onOpenLicence: () -> Unit,
    onOpenSitePage: (String) -> Unit,
    onOpenPortalInfo: (PortalInfo) -> Unit,
) {
    val listState = rememberLazyListState()
    val pad = responsiveContentPadding()
    val columns = responsiveGridColumns(compact = 1, medium = 2, expanded = 3)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(bottom = 48.dp),
    ) {
        item {
            HomeHero(
                onExplore = onOpenProvinces,
                onHowItWorks = {
                    onOpenSitePage(AppContent.siteUrl("how-it-works.html"))
                },
            )
        }

        item {
            Column(
                modifier = Modifier
                    .padding(top = 28.dp)
                    .enterFadeUp(delayMs = 70),
            ) {
                Column(modifier = Modifier.padding(horizontal = pad)) {
                    SectionHeader(
                        title = stringResource(id = R.string.section_provinces),
                        subtitle = stringResource(id = R.string.section_provinces_sub),
                        actionLabel = stringResource(id = R.string.see_all),
                        onAction = onOpenProvinces,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (columns > 1) {
                    ProvinceGrid(
                        provinces = provinces,
                        columns = columns,
                        onProvinceClick = onProvinceClick,
                        pad = pad,
                    )
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = pad),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        itemsIndexed(provinces, key = { _, p -> p.id }) { index, province ->
                            ProvinceCard(
                                name = province.name,
                                badge = province.badge,
                                description = province.description,
                                icon = RegionIcons.forProvince(province.id),
                                onClick = { onProvinceClick(province.id) },
                                compact = true,
                                modifier = Modifier
                                    .width(280.dp)
                                    .enterFade(delayMs = 60 + index * 40),
                            )
                        }
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = pad)
                    .padding(top = 32.dp)
                    .enterFadeUp(delayMs = 120),
            ) {
                SectionHeader(
                    title = stringResource(id = R.string.section_categories),
                    subtitle = stringResource(id = R.string.section_categories_sub),
                    actionLabel = stringResource(id = R.string.see_all),
                    onAction = onOpenServices,
                )
                Spacer(modifier = Modifier.height(16.dp))
                CategoryIconGrid(
                    guides = guides,
                    onOpenLicence = onOpenLicence,
                    onOpenSitePage = onOpenSitePage,
                    onOpenPortalInfo = onOpenPortalInfo,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(id = R.string.independent_notice_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = SeaDeep,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.independent_notice_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(14.dp))
                AffiliationDisclaimer()
            }
        }
    }
}

@Composable
private fun HomeHero(
    onExplore: () -> Unit,
    onHowItWorks: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFCDEEDB), Sand, Fog),
                ),
            ),
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Sea.copy(alpha = 0.18f), Color.Transparent),
                    ),
                ),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = responsiveContentPadding(), vertical = 40.dp)
                .enterFadeUp(),
        ) {
            Text(
                text = stringResource(id = R.string.hero_kicker),
                style = MaterialTheme.typography.labelLarge,
                color = SeaDeep,
            )
            Spacer(modifier = Modifier.height(20.dp))
            SpinningBrandLogo(size = 88.dp, durationMs = 4200, elevated = true)
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displaySmall,
                color = Ink,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(id = R.string.hero_tagline),
                style = MaterialTheme.typography.titleLarge,
                color = SeaDeep,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.hero_support),
                style = MaterialTheme.typography.bodyLarge,
                color = Ink.copy(alpha = 0.78f),
            )
            Spacer(modifier = Modifier.height(22.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PrimaryButton(
                    text = stringResource(id = R.string.cta_explore_services),
                    onClick = onExplore,
                )
                GhostButton(
                    text = stringResource(id = R.string.cta_verification_guide),
                    onClick = onHowItWorks,
                )
            }
        }
    }
}

@Composable
private fun ProvinceGrid(
    provinces: List<Province>,
    columns: Int,
    onProvinceClick: (String) -> Unit,
    pad: Dp,
) {
    Column(
        modifier = Modifier.padding(horizontal = pad),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        provinces.chunked(columns).forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                row.forEachIndexed { colIndex, province ->
                    ProvinceCard(
                        name = province.name,
                        badge = province.badge,
                        description = province.description,
                        icon = RegionIcons.forProvince(province.id),
                        onClick = { onProvinceClick(province.id) },
                        modifier = Modifier
                            .weight(1f)
                            .enterFadeUp(delayMs = 50 + (rowIndex * columns + colIndex) * 35),
                    )
                }
                repeat(columns - row.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun CategoryIconGrid(
    guides: List<GuideItem>,
    onOpenLicence: () -> Unit,
    onOpenSitePage: (String) -> Unit,
    onOpenPortalInfo: (PortalInfo) -> Unit,
) {
    val context = LocalContext.current
    val columns = responsiveGridColumns(compact = 2, medium = 3, expanded = 4)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        guides.chunked(columns).forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                row.forEachIndexed { colIndex, guide ->
                    CompactCategoryTile(
                        title = guide.title,
                        iconRes = guide.iconRes,
                        onClick = {
                            when {
                                guide.id == "licence" -> onOpenLicence()
                                UrlHelpers.isGovernmentSource(guide.officialUrl) -> onOpenPortalInfo(
                                    PortalInfo.forGuide(
                                        guide = guide,
                                        openCta = guideOpenCta(context, guide.id),
                                    ),
                                )
                                else -> onOpenSitePage(guide.officialUrl)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .enterFadeUp(delayMs = 80 + (rowIndex * columns + colIndex) * 35),
                    )
                }
                repeat(columns - row.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}
