package com.pakexciseinfo.app.ui.provinces

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
import com.pakexciseinfo.app.data.Province
import com.pakexciseinfo.app.ui.components.ProvinceCard

@OptIn(ExperimentalMaterial3Api::class)
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

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.nav_provinces)) },
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
            label = { Text(text = stringResource(id = R.string.search_provinces)) },
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.section_provinces_sub),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
            items(items = filtered, key = { it.id }) { province ->
                ProvinceCard(
                    name = province.name,
                    badge = province.badge,
                    description = province.description,
                    logoRes = province.logoRes,
                    onClick = { onProvinceClick(province.id) },
                    modifier = Modifier.padding(vertical = 6.dp),
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
