package com.pakexciseinfo.app.ui.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.pakexciseinfo.app.R
import com.pakexciseinfo.app.data.AppContent
import com.pakexciseinfo.app.data.GuideItem
import com.pakexciseinfo.app.ui.components.AppSearchField
import com.pakexciseinfo.app.ui.components.CategoryCard
import com.pakexciseinfo.app.ui.components.ScreenHeader
import com.pakexciseinfo.app.ui.components.enterFadeUp
import com.pakexciseinfo.app.ui.components.responsiveContentPadding

@Composable
fun ServicesScreen(
    guides: List<GuideItem>,
    onOpenOfficial: (String) -> Unit,
    onOpenGuide: (String) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    val filtered = guides.filter { guide ->
        query.isBlank() ||
            guide.title.contains(query, ignoreCase = true) ||
            guide.description.contains(query, ignoreCase = true)
    }
    val listState = rememberLazyListState()
    val pad = responsiveContentPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = pad),
    ) {
        ScreenHeader(
            title = stringResource(id = R.string.nav_services),
            subtitle = stringResource(id = R.string.section_categories_sub),
        )
        AppSearchField(
            value = query,
            onValueChange = { query = it },
            placeholder = stringResource(id = R.string.search_services),
            modifier = Modifier.enterFadeUp(delayMs = 40),
        )
        Spacer(modifier = Modifier.height(14.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            itemsIndexed(filtered, key = { _, g -> g.id }) { index, guide ->
                CategoryCard(
                    title = guide.title,
                    description = guide.description,
                    iconRes = guide.iconRes,
                    onClick = { onOpenOfficial(guide.officialUrl) },
                    actionHint = stringResource(id = R.string.open_service),
                    secondaryAction = stringResource(id = R.string.read_guide),
                    onSecondaryAction = { onOpenGuide(AppContent.siteUrl(guide.guidePath)) },
                    modifier = Modifier.enterFadeUp(delayMs = 50 + index * 40),
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
