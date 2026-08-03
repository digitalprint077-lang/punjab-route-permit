package com.pakexciseinfo.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pakexciseinfo.app.ui.AppViewModel
import com.pakexciseinfo.app.ui.home.HomeScreen
import com.pakexciseinfo.app.ui.more.MoreScreen
import com.pakexciseinfo.app.ui.provinces.ProvinceDetailScreen
import com.pakexciseinfo.app.ui.provinces.ProvincesScreen
import com.pakexciseinfo.app.ui.services.ServicesScreen
import com.pakexciseinfo.app.ui.theme.PakExciseTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PakExciseTheme {
                AppRoot()
            }
        }
    }
}

private data class TopDest(
    val route: String,
    val labelRes: Int,
    val icon: ImageVector,
)

@Composable
private fun AppRoot(
    viewModel: AppViewModel = viewModel(),
) {
    val topDestinations = remember {
        listOf(
            TopDest("home", R.string.nav_home, Icons.Rounded.Home),
            TopDest("provinces", R.string.nav_provinces, Icons.Rounded.Public),
            TopDest("services", R.string.nav_services, Icons.Rounded.Apps),
            TopDest("more", R.string.nav_more, Icons.Rounded.MoreHoriz),
        )
    }

    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentDestination = backStack?.destination
    val showBottomBar = topDestinations.any { dest ->
        currentDestination?.hierarchy?.any { it.route == dest.route } == true
    }
    val content by viewModel.content.collectAsStateWithLifecycle()
    val opening by viewModel.opening.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.messages.collect { messageRes ->
            snackbarHostState.showSnackbar(message = context.getString(messageRes))
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    topDestinations.forEach { dest ->
                        val selected =
                            currentDestination?.hierarchy?.any { it.route == dest.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(dest.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(imageVector = dest.icon, contentDescription = null) },
                            label = { Text(text = stringResource(id = dest.labelRes)) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.fillMaxSize(),
            ) {
                composable(route = "home") {
                    HomeScreen(
                        guides = content.guides,
                        provinces = content.provinces,
                        onProvinceClick = { id -> navController.navigate("province/$id") },
                        onOpenProvinces = {
                            navController.navigate("provinces") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onOpenServices = {
                            navController.navigate("services") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onOpenOfficial = viewModel::openUrl,
                    )
                }
                composable(route = "provinces") {
                    ProvincesScreen(
                        provinces = content.provinces,
                        onProvinceClick = { id -> navController.navigate("province/$id") },
                    )
                }
                composable(route = "services") {
                    ServicesScreen(
                        guides = content.guides,
                        onOpenOfficial = viewModel::openUrl,
                        onOpenGuide = viewModel::openUrl,
                    )
                }
                composable(route = "more") {
                    MoreScreen(
                        onOpenUrl = viewModel::openUrl,
                        onRefreshConfig = viewModel::refreshConfig,
                    )
                }
                composable(
                    route = "province/{provinceId}",
                    arguments = listOf(
                        navArgument(name = "provinceId") { type = NavType.StringType },
                    ),
                ) { entry ->
                    val id = entry.arguments?.getString("provinceId").orEmpty()
                    ProvinceDetailScreen(
                        province = content.provinceById(id),
                        onBack = { navController.popBackStack() },
                        onOpenOfficial = viewModel::openUrl,
                        onOpenGuide = viewModel::openUrl,
                    )
                }
            }

            if (opening) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
