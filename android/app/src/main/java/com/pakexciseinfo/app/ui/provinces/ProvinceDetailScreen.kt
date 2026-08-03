package com.pakexciseinfo.app.ui.provinces

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pakexciseinfo.app.R
import com.pakexciseinfo.app.data.AppContent
import com.pakexciseinfo.app.data.Province
import com.pakexciseinfo.app.ui.components.CategoryCard
import com.pakexciseinfo.app.ui.components.SectionHeader
import com.pakexciseinfo.app.ui.theme.Sand
import com.pakexciseinfo.app.ui.theme.Sea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvinceDetailScreen(
    province: Province?,
    onBack: () -> Unit,
    onOpenOfficial: (String) -> Unit,
    onOpenGuide: (String) -> Unit,
) {
    if (province == null) {
        Column(modifier = Modifier.fillMaxSize().padding(all = 20.dp)) {
            Text(text = stringResource(id = R.string.not_found))
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onBack) {
                Text(text = stringResource(id = R.string.go_back))
            }
        }
        return
    }

    val listState = rememberLazyListState()
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = province.name) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(id = R.string.go_back),
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(bottom = 28.dp),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(color = Sand.copy(alpha = 0.65f))
                        .padding(all = 18.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = province.logoRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(color = MaterialTheme.colorScheme.surface),
                            contentScale = ContentScale.Fit,
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = province.badge.uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                                color = Sea,
                            )
                            Text(
                                text = province.name,
                                style = MaterialTheme.typography.headlineSmall,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = province.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row {
                        Button(onClick = { onOpenOfficial(province.portalUrl) }) {
                            Text(text = stringResource(id = R.string.visit_portal))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        OutlinedButton(
                            onClick = { onOpenGuide(AppContent.siteUrl(province.guidePath)) },
                        ) {
                            Text(text = stringResource(id = R.string.read_guide))
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
                    SectionHeader(
                        title = stringResource(id = R.string.section_service_categories),
                        subtitle = stringResource(id = R.string.section_service_categories_sub),
                    )
                }
            }

            items(items = province.services, key = { it.id }) { service ->
                CategoryCard(
                    title = service.title,
                    description = service.description,
                    iconRes = service.iconRes,
                    onClick = { onOpenOfficial(service.officialUrl) },
                    actionHint = stringResource(id = R.string.open_service),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                )
            }
        }
    }
}
