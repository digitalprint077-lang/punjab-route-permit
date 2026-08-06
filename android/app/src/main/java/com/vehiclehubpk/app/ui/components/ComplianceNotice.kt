package com.vehiclehubpk.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vehiclehubpk.app.R
import com.vehiclehubpk.app.data.UrlHelpers
import com.vehiclehubpk.app.ui.theme.Sand
import com.vehiclehubpk.app.ui.theme.Sea
import com.vehiclehubpk.app.ui.theme.SeaDeep

@Composable
fun OfficialSourceBlock(
    sourceUrl: String,
    authorityName: String,
    modifier: Modifier = Modifier,
    onOpenSource: ((String) -> Unit)? = null,
    forceOfficialLabel: Boolean? = null,
) {
    val isOfficial = forceOfficialLabel ?: UrlHelpers.isGovernmentSource(sourceUrl)
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(
                id = if (isOfficial) R.string.official_source_label else R.string.related_source_label,
            ),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = sourceUrl,
            style = MaterialTheme.typography.bodyMedium.copy(
                textDecoration = if (onOpenSource != null) TextDecoration.Underline else TextDecoration.None,
                fontWeight = FontWeight.SemiBold,
            ),
            color = Sea,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = if (onOpenSource != null) {
                Modifier
                    .fillMaxWidth()
                    .clickable { onOpenSource(sourceUrl) }
                    .padding(vertical = 4.dp)
            } else {
                Modifier
            },
        )
        if (onOpenSource != null) {
            Text(
                text = stringResource(id = R.string.tap_to_open_source),
                style = MaterialTheme.typography.bodyMedium,
                color = SeaDeep.copy(alpha = 0.75f),
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        if (authorityName.isNotBlank()) {
            Text(
                text = stringResource(id = R.string.official_authority_label, authorityName),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun AffiliationDisclaimer(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Sand,
    ) {
        Text(
            text = stringResource(id = R.string.affiliation_disclaimer),
            style = MaterialTheme.typography.bodyMedium,
            color = SeaDeep,
            modifier = Modifier.padding(14.dp),
        )
    }
}
