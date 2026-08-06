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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vehiclehubpk.app.R
import com.vehiclehubpk.app.ui.theme.Sand
import com.vehiclehubpk.app.ui.theme.Sea
import com.vehiclehubpk.app.ui.theme.SeaDeep

@Composable
fun OfficialSourceBlock(
    sourceUrl: String,
    authorityName: String,
    modifier: Modifier = Modifier,
    onOpenSource: ((String) -> Unit)? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.official_source_label, sourceUrl),
            style = MaterialTheme.typography.bodyMedium,
            color = Sea,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = if (onOpenSource != null) {
                Modifier.clickable { onOpenSource(sourceUrl) }
            } else {
                Modifier
            },
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.official_authority_label, authorityName),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
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
