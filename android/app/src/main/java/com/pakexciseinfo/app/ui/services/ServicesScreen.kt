package com.pakexciseinfo.app.ui.services

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.pakexciseinfo.app.ui.components.CategoryCard

@OptIn(ExperimentalMaterial3Api::class)
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

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.nav_services)) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        )
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.search_services)) },
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.section_categories_sub),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
            items(items = filtered, key = { it.id }) { guide ->
                CategoryCard(
                    title = guide.title,
                    description = guide.description,
                    iconRes = guide.iconRes,
                    onClick = { onOpenOfficial(guide.officialUrl) },
                    actionHint = stringResource(id = R.string.open_service),
                    modifier = Modifier.padding(vertical = 6.dp),
                )
                CategoryCard(
                    title = stringResource(id = R.string.guide_for, guide.title),
                    description = stringResource(id = R.string.read_guide_hint),
                    iconRes = R.drawable.ic_cat_portal,
                    onClick = { onOpenGuide(AppContent.siteUrl(guide.guidePath)) },
                    actionHint = stringResource(id = R.string.read_guide),
                    modifier = Modifier.padding(bottom = 10.dp),
                )
            }
            if (filtered.isEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.search_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 20.dp),
                    )
                }
            }
        }
    }
}
