package com.vehiclehubpk.app.ui.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vehiclehubpk.app.R
import com.vehiclehubpk.app.data.PortalInfo
import com.vehiclehubpk.app.ui.components.AffiliationDisclaimer
import com.vehiclehubpk.app.ui.components.OfficialSourceBlock
import com.vehiclehubpk.app.ui.components.PrimaryButton
import com.vehiclehubpk.app.ui.components.ScreenHeader
import com.vehiclehubpk.app.ui.components.enterFadeUp
import com.vehiclehubpk.app.ui.components.responsiveContentPadding
import com.vehiclehubpk.app.ui.theme.Ink
import com.vehiclehubpk.app.ui.theme.SeaDeep

@Composable
fun ServiceInfoScreen(
    info: PortalInfo?,
    onBack: () -> Unit,
    onOpenOfficialWebsite: (String) -> Unit,
) {
    val pad = responsiveContentPadding()
    if (info == null) {
        Column(modifier = Modifier.fillMaxSize().padding(pad)) {
            ScreenHeader(title = stringResource(id = R.string.not_found), onBack = onBack)
            PrimaryButton(text = stringResource(id = R.string.go_back), onClick = onBack)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = pad),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ScreenHeader(
                title = info.title,
                subtitle = stringResource(id = R.string.service_info_subtitle),
                onBack = onBack,
            )
        }
        item {
            Column(modifier = Modifier.enterFadeUp(delayMs = 40)) {
                Text(
                    text = stringResource(id = R.string.service_info_source_heading),
                    style = MaterialTheme.typography.labelLarge,
                    color = SeaDeep,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = info.authorityName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Ink,
                )
                if (info.detail.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = info.detail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        item {
            OfficialSourceBlock(
                sourceUrl = info.officialUrl,
                authorityName = info.authorityName,
                onOpenSource = null, // open only via the CTA below
                modifier = Modifier.enterFadeUp(delayMs = 70),
            )
        }
        item {
            Text(
                text = stringResource(id = R.string.service_info_leave_app),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.enterFadeUp(delayMs = 90),
            )
        }
        item {
            AffiliationDisclaimer(modifier = Modifier.enterFadeUp(delayMs = 110))
        }
        item {
            Spacer(modifier = Modifier.height(4.dp))
            PrimaryButton(
                text = info.openCta.ifBlank { stringResource(id = R.string.open_official_gov_website) },
                onClick = { onOpenOfficialWebsite(info.officialUrl) },
                modifier = Modifier
                    .fillMaxWidth()
                    .enterFadeUp(delayMs = 130),
            )
        }
    }
}
