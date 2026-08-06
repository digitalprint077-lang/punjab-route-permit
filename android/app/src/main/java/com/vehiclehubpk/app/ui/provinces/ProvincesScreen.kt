package com.vehiclehubpk.app.ui.provinces

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vehiclehubpk.app.R
import com.vehiclehubpk.app.data.Province
import com.vehiclehubpk.app.ui.components.AppSearchField
import com.vehiclehubpk.app.ui.components.AffiliationDisclaimer
import com.vehiclehubpk.app.ui.components.ProvinceCard
import com.vehiclehubpk.app.ui.components.RegionIcons
import com.vehiclehubpk.app.ui.components.ScreenHeader
import com.vehiclehubpk.app.ui.components.enterFadeUp
import com.vehiclehubpk.app.ui.components.responsiveContentPadding
import com.vehiclehubpk.app.ui.components.responsiveGridColumns

@Composable
fun ProvincesScreen(
    provinces: List<Province>,
    onProvinceClick: (String) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    val filtered = provinces.filter { province ->
        query.isBlank() ||
            province.name.contains(query, ignoreCase = true) ||
            province.badge.contains(query, ignoreCase = true) ||
            province.description.contains(query, ignoreCase = true)
    }
    val listState = rememberLazyListState()
    val pad = responsiveContentPadding()
    val columns = responsiveGridColumns(compact = 1, medium = 2, expanded = 3)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = pad),
    ) {
        ScreenHeader(
            title = stringResource(id = R.string.nav_provinces),
            subtitle = stringResource(id = R.string.section_provinces_sub),
        )
        AppSearchField(
            value = query,
            onValueChange = { query = it },
            placeholder = stringResource(id = R.string.search_provinces),
            modifier = Modifier.enterFadeUp(delayMs = 40),
        )
        Spacer(modifier = Modifier.height(14.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (columns == 1) {
                itemsIndexed(filtered, key = { _, p -> p.id }) { index, province ->
                    ProvinceCard(
                        name = province.name,
                        badge = province.badge,
                        description = province.description,
                        icon = RegionIcons.forProvince(province.id),
                        onClick = { onProvinceClick(province.id) },
                        modifier = Modifier.enterFadeUp(delayMs = 50 + index * 35),
                    )
                }
            } else {
                val rows = filtered.chunked(columns)
                itemsIndexed(rows, key = { index, _ -> "row-$index" }) { rowIndex, row ->
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
            
            item {
                AffiliationDisclaimer(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .enterFadeUp(delayMs = 80),
                )
            }

            if (filtered.isEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.search_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 24.dp),
                    )
                }
            }
        }
    }
}
