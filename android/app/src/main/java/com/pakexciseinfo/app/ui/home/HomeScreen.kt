package com.pakexciseinfo.app.ui.home

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pakexciseinfo.app.R
import com.pakexciseinfo.app.data.AppContent
import com.pakexciseinfo.app.data.GuideItem
import com.pakexciseinfo.app.data.Province
import com.pakexciseinfo.app.ui.components.CategoryCard
import com.pakexciseinfo.app.ui.components.CompactCategoryTile
import com.pakexciseinfo.app.ui.components.ProvinceCard
import com.pakexciseinfo.app.ui.components.SectionHeader
import com.pakexciseinfo.app.ui.components.enterFadeUp
import com.pakexciseinfo.app.ui.components.responsiveContentPadding
import com.pakexciseinfo.app.ui.components.responsiveGridColumns
import com.pakexciseinfo.app.ui.theme.Fog
import com.pakexciseinfo.app.ui.theme.Ink
import com.pakexciseinfo.app.ui.theme.Sand
import com.pakexciseinfo.app.ui.theme.Sea
import com.pakexciseinfo.app.ui.theme.SeaDeep

@Composable
fun HomeScreen(
    guides: List<GuideItem>,
    provinces: List<Province>,
    onProvinceClick: (String) -> Unit,
    onOpenProvinces: () -> Unit,
    onOpenServices: () -> Unit,
    onOpenOfficial: (String) -> Unit,
) {
    val listState = rememberLazyListState()
    val pad = responsiveContentPadding()
    val provinceCols = responsiveGridColumns(compact = 1, medium = 2, expanded = 3)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(bottom = 32.dp),
    ) {
        item {
            HomeHero(
                onExplore = onOpenProvinces,
                onVerification = {
                    onOpenOfficial(AppContent.siteUrl("guides/vehicle-verification.html"))
                },
            )
        }

        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = pad)
                    .padding(top = 28.dp, bottom = 8.dp)
                    .enterFadeUp(delayMs = 60),
            ) {
                SectionHeader(
                    title = stringResource(id = R.string.section_provinces),
                    subtitle = stringResource(id = R.string.section_provinces_sub),
                    actionLabel = stringResource(id = R.string.see_all),
                    onAction = onOpenProvinces,
                )
                Spacer(modifier = Modifier.height(16.dp))
                ProvinceGrid(
                    provinces = provinces,
                    columns = provinceCols,
                    onProvinceClick = onProvinceClick,
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = pad)
                    .padding(top = 28.dp, bottom = 8.dp)
                    .enterFadeUp(delayMs = 120),
            ) {
                SectionHeader(
                    title = stringResource(id = R.string.section_categories),
                    subtitle = stringResource(id = R.string.section_categories_sub),
                    actionLabel = stringResource(id = R.string.see_all),
                    onAction = onOpenServices,
                )
                Spacer(modifier = Modifier.height(16.dp))
                CategoryIconGrid(guides = guides, onOpenOfficial = onOpenOfficial)
            }
        }

        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = pad)
                    .padding(top = 28.dp, bottom = 8.dp)
                    .enterFadeUp(delayMs = 160),
            ) {
                SectionHeader(
                    title = stringResource(id = R.string.section_quick_services),
                    subtitle = stringResource(id = R.string.section_quick_services_sub),
                )
            }
        }

        itemsIndexed(items = guides, key = { _, g -> "svc-${g.id}" }) { index, guide ->
            CategoryCard(
                title = guide.title,
                description = guide.description,
                iconRes = guide.iconRes,
                onClick = { onOpenOfficial(guide.officialUrl) },
                actionHint = stringResource(id = R.string.open_service),
                modifier = Modifier
                    .padding(horizontal = pad, vertical = 6.dp)
                    .enterFadeUp(delayMs = 40 + index * 35),
            )
        }
    }
}

@Composable
private fun HomeHero(
    onExplore: () -> Unit,
    onVerification: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFD8F0E2),
                        Sand,
                        Fog,
                    ),
                ),
            ),
    ) {
        // Soft atmospheric wash (not a card)
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Sea.copy(alpha = 0.14f),
                            Color.Transparent,
                        ),
                    ),
                ),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 720.dp)
                .align(Alignment.CenterStart)
                .padding(horizontal = responsiveContentPadding(), vertical = 36.dp)
                .enterFadeUp(delayMs = 0),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = stringResource(id = R.string.hero_kicker),
                style = MaterialTheme.typography.labelLarge,
                color = SeaDeep,
            )
            Spacer(modifier = Modifier.height(18.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_brand),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .size(84.dp)
                    .shadow(elevation = 12.dp, shape = CircleShape, clip = false)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.height(16.dp))
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Button(
                    onClick = onExplore,
                    colors = ButtonDefaults.buttonColors(containerColor = Sea),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.height(50.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                ) {
                    Text(text = stringResource(id = R.string.cta_explore_services))
                }
                OutlinedButton(
                    onClick = onVerification,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SeaDeep),
                ) {
                    Text(text = stringResource(id = R.string.cta_verification_guide))
                }
            }
        }
    }
}

@Composable
private fun ProvinceGrid(
    provinces: List<Province>,
    columns: Int,
    onProvinceClick: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        provinces.chunked(columns).forEachIndexed { rowIndex, rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowItems.forEachIndexed { colIndex, province ->
                    ProvinceCard(
                        name = province.name,
                        badge = province.badge,
                        description = province.description,
                        logoRes = province.logoRes,
                        onClick = { onProvinceClick(province.id) },
                        modifier = Modifier
                            .weight(1f)
                            .enterFadeUp(delayMs = 50 + (rowIndex * columns + colIndex) * 40),
                    )
                }
                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CategoryIconGrid(
    guides: List<GuideItem>,
    onOpenOfficial: (String) -> Unit,
) {
    val columns = responsiveGridColumns(compact = 2, medium = 3, expanded = 4)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        guides.chunked(columns).forEachIndexed { rowIndex, rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowItems.forEachIndexed { colIndex, guide ->
                    CompactCategoryTile(
                        title = guide.title,
                        iconRes = guide.iconRes,
                        onClick = { onOpenOfficial(guide.officialUrl) },
                        modifier = Modifier
                            .weight(1f)
                            .enterFadeUp(delayMs = 70 + (rowIndex * columns + colIndex) * 35),
                    )
                }
                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
