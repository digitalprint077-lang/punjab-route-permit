package com.pakexciseinfo.app.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
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
import com.pakexciseinfo.app.ui.components.CategoryCard
import com.pakexciseinfo.app.ui.components.CompactCategoryTile
import com.pakexciseinfo.app.ui.components.ProvinceCard
import com.pakexciseinfo.app.ui.components.SectionHeader
import com.pakexciseinfo.app.ui.theme.Fog
import com.pakexciseinfo.app.ui.theme.Ink
import com.pakexciseinfo.app.ui.theme.Sand
import com.pakexciseinfo.app.ui.theme.Sea
import com.pakexciseinfo.app.ui.theme.SeaDeep

@Composable
fun HomeScreen(
    onProvinceClick: (String) -> Unit,
    onOpenProvinces: () -> Unit,
    onOpenServices: () -> Unit,
    onOpenOfficial: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 28.dp),
    ) {
        item { HomeHero() }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
                SectionHeader(
                    title = stringResource(id = R.string.section_categories),
                    subtitle = stringResource(id = R.string.section_categories_sub),
                    actionLabel = stringResource(id = R.string.see_all),
                    onAction = onOpenServices,
                )
                Spacer(modifier = Modifier.height(14.dp))
                CategoryIconGrid(onOpenOfficial = onOpenOfficial)
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                SectionHeader(
                    title = stringResource(id = R.string.section_provinces),
                    subtitle = stringResource(id = R.string.section_provinces_sub),
                    actionLabel = stringResource(id = R.string.see_all),
                    onAction = onOpenProvinces,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        items(items = AppContent.provinces, key = { it.id }) { province ->
            ProvinceCard(
                name = province.name,
                badge = province.badge,
                description = province.description,
                logoRes = province.logoRes,
                onClick = { onProvinceClick(province.id) },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
            )
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
                SectionHeader(
                    title = stringResource(id = R.string.section_quick_services),
                    subtitle = stringResource(id = R.string.section_quick_services_sub),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        items(items = AppContent.popularGuides, key = { "svc-${it.id}" }) { guide ->
            CategoryCard(
                title = guide.title,
                description = guide.description,
                iconRes = guide.iconRes,
                onClick = { onOpenOfficial(guide.officialUrl) },
                actionHint = stringResource(id = R.string.open_service),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
            )
        }
    }
}

@Composable
private fun HomeHero() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Sand, Fog, Color.White),
                ),
            )
            .padding(horizontal = 20.dp, vertical = 28.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_brand),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Fit,
            )
            Spacer(modifier = Modifier.size(14.dp))
            Column {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Ink,
                )
                Text(
                    text = stringResource(id = R.string.hero_tagline),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SeaDeep,
                )
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = stringResource(id = R.string.hero_support),
            style = MaterialTheme.typography.bodyLarge,
            color = Ink.copy(alpha = 0.78f),
        )
    }
}

@Composable
private fun CategoryIconGrid(onOpenOfficial: (String) -> Unit) {
    val guides = AppContent.popularGuides
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        guides.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowItems.forEach { guide ->
                    CompactCategoryTile(
                        title = guide.title,
                        iconRes = guide.iconRes,
                        onClick = { onOpenOfficial(guide.officialUrl) },
                        modifier = Modifier.weight(1f),
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
