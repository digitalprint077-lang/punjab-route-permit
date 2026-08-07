package com.vehiclehubpk.app.ui.licence

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vehiclehubpk.app.R
import com.vehiclehubpk.app.data.AppContent
import com.vehiclehubpk.app.data.LicencePortal
import com.vehiclehubpk.app.data.PortalInfo
import com.vehiclehubpk.app.ui.components.AffiliationDisclaimer
import com.vehiclehubpk.app.ui.components.CategoryCard
import com.vehiclehubpk.app.ui.components.GhostButton
import com.vehiclehubpk.app.ui.components.PrimaryButton
import com.vehiclehubpk.app.ui.components.RegionIcons
import com.vehiclehubpk.app.ui.components.ScreenHeader
import com.vehiclehubpk.app.ui.components.SectionHeader
import com.vehiclehubpk.app.ui.components.enterFadeUp
import com.vehiclehubpk.app.ui.components.responsiveContentPadding
import com.vehiclehubpk.app.ui.theme.SeaDeep

@Composable
fun LicenceScreen(
    licences: List<LicencePortal>,
    onOpenSitePage: (String) -> Unit,
    onOpenPortalInfo: (PortalInfo) -> Unit,
) {
    val pad = responsiveContentPadding()
    val listState = rememberLazyListState()
    val licenceCta = stringResource(id = R.string.cta_open_driving_licence)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = pad),
    ) {
        ScreenHeader(
            title = stringResource(id = R.string.nav_licence),
            subtitle = stringResource(id = R.string.section_licence_sub),
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .enterFadeUp(delayMs = 40),
                ) {
                    Text(
                        text = stringResource(id = R.string.licence_intro),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    AffiliationDisclaimer()
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        PrimaryButton(
                            text = stringResource(id = R.string.licence_open_guide),
                            onClick = { onOpenSitePage(AppContent.siteUrl(AppContent.LICENCE_GUIDE_PATH)) },
                        )
                        GhostButton(
                            text = stringResource(id = R.string.licence_verify_jump),
                            onClick = {
                                onOpenSitePage(
                                    AppContent.siteUrl("${AppContent.LICENCE_GUIDE_PATH}#verify"),
                                )
                            },
                        )
                    }
                    Spacer(modifier = Modifier.height(22.dp))
                    SectionHeader(
                        title = stringResource(id = R.string.section_licence_portals),
                        subtitle = stringResource(id = R.string.section_licence_portals_sub),
                    )
                }
            }

            itemsIndexed(licences, key = { _, item -> item.id }) { index, portal ->
                CategoryCard(
                    title = portal.title,
                    description = "${portal.region} · ${portal.description}",
                    icon = RegionIcons.forLicence(portal.id),
                    onClick = {
                        onOpenPortalInfo(
                            PortalInfo(
                                title = portal.title,
                                authorityName = portal.authorityName,
                                officialUrl = portal.verifyUrl,
                                detail = portal.description,
                                openCta = licenceCta,
                            ),
                        )
                    },
                    actionHint = stringResource(id = R.string.licence_verify),
                    secondaryAction = stringResource(id = R.string.visit_portal),
                    onSecondaryAction = {
                        onOpenPortalInfo(
                            PortalInfo(
                                title = portal.title,
                                authorityName = portal.authorityName,
                                officialUrl = portal.portalUrl,
                                detail = portal.description,
                                openCta = licenceCta,
                            ),
                        )
                    },
                    officialSourceUrl = portal.verifyUrl,
                    authorityName = portal.authorityName,
                    onOpenOfficialSource = {
                        onOpenPortalInfo(
                            PortalInfo(
                                title = portal.title,
                                authorityName = portal.authorityName,
                                officialUrl = portal.verifyUrl,
                                detail = portal.description,
                                openCta = licenceCta,
                            ),
                        )
                    },
                    modifier = Modifier.enterFadeUp(delayMs = 50 + index * 35),
                )
            }

            item {
                Text(
                    text = stringResource(id = R.string.licence_note),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SeaDeep,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .enterFadeUp(delayMs = 120),
                )
            }
            item {
                AffiliationDisclaimer(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 12.dp)
                        .enterFadeUp(delayMs = 140),
                )
            }
        }
    }
}
