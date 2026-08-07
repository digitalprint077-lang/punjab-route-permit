package com.vehiclehubpk.app.ui.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vehiclehubpk.app.R
import com.vehiclehubpk.app.data.GovernmentSource
import com.vehiclehubpk.app.data.PortalInfo
import com.vehiclehubpk.app.ui.components.AffiliationDisclaimer
import com.vehiclehubpk.app.ui.components.CategoryCard
import com.vehiclehubpk.app.ui.components.RegionIcons
import com.vehiclehubpk.app.ui.components.ScreenHeader
import com.vehiclehubpk.app.ui.components.enterFadeUp
import com.vehiclehubpk.app.ui.components.responsiveContentPadding
import com.vehiclehubpk.app.ui.theme.SeaDeep

@Composable
fun SourcesScreen(
    sources: List<GovernmentSource>,
    onBack: () -> Unit,
    onOpenSourceInfo: (PortalInfo) -> Unit,
) {
    val pad = responsiveContentPadding()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = pad),
    ) {
        ScreenHeader(
            title = stringResource(id = R.string.nav_sources),
            subtitle = stringResource(id = R.string.sources_subtitle),
            onBack = onBack,
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.sources_intro),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.enterFadeUp(delayMs = 40),
                )
            }
            item {
                AffiliationDisclaimer(modifier = Modifier.enterFadeUp(delayMs = 60))
            }
            itemsIndexed(sources, key = { _, s -> s.websiteUrl }) { index, source ->
                CategoryCard(
                    title = source.region,
                    description = source.department,
                    icon = RegionIcons.forProvince(
                        when {
                            source.region.contains("Punjab", true) -> "punjab"
                            source.region.contains("Sindh", true) -> "sindh"
                            source.region.contains("Islamabad", true) -> "islamabad"
                            source.region.contains("KPK", true) || source.region.contains("Khyber", true) -> "kpk"
                            source.region.contains("Balochistan", true) || source.region.contains("Quetta", true) -> "balochistan"
                            source.region.contains("Gilgit", true) -> "gilgit"
                            else -> "punjab"
                        },
                    ),
                    onClick = {
                        onOpenSourceInfo(
                            PortalInfo(
                                title = source.region,
                                authorityName = source.department,
                                officialUrl = source.websiteUrl,
                                detail = "Directory entry for this official government website.",
                            ),
                        )
                    },
                    actionHint = stringResource(id = R.string.open_official_gov_website),
                    officialSourceUrl = source.websiteUrl,
                    authorityName = source.department,
                    onOpenOfficialSource = {
                        onOpenSourceInfo(
                            PortalInfo(
                                title = source.region,
                                authorityName = source.department,
                                officialUrl = source.websiteUrl,
                            ),
                        )
                    },
                    modifier = Modifier.enterFadeUp(delayMs = 50 + index * 30),
                )
            }
            item {
                Text(
                    text = stringResource(id = R.string.sources_footer),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SeaDeep,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .enterFadeUp(delayMs = 100),
                )
            }
        }
    }
}
