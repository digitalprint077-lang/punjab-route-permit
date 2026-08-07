package com.vehiclehubpk.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.vehiclehubpk.app.ads.BannerAd
import com.vehiclehubpk.app.data.AppContent
import com.vehiclehubpk.app.data.PortalInfo
import com.vehiclehubpk.app.ui.AppViewModel
import com.vehiclehubpk.app.ui.components.AnimatedBottomBar
import com.vehiclehubpk.app.ui.components.BottomTab
import com.vehiclehubpk.app.ui.home.HomeScreen
import com.vehiclehubpk.app.ui.info.ServiceInfoScreen
import com.vehiclehubpk.app.ui.info.SourcesScreen
import com.vehiclehubpk.app.ui.licence.LicenceScreen
import com.vehiclehubpk.app.ui.more.MoreScreen
import com.vehiclehubpk.app.ui.provinces.ProvinceDetailScreen
import com.vehiclehubpk.app.ui.provinces.ProvincesScreen
import com.vehiclehubpk.app.ui.services.ServicesScreen
import com.vehiclehubpk.app.ui.theme.Sea
import com.vehiclehubpk.app.ui.theme.VehicleHubTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VehicleHubTheme {
                AppRoot()
            }
        }
    }
}

@Composable
private fun AppRoot(
    viewModel: AppViewModel = viewModel(),
) {
    val tabs = listOf(
        BottomTab("home", stringResource(R.string.nav_home), Icons.Rounded.Home),
        BottomTab("licence", stringResource(R.string.nav_licence), Icons.Rounded.Badge),
        BottomTab("provinces", stringResource(R.string.nav_provinces), Icons.Rounded.Map),
        BottomTab("services", stringResource(R.string.nav_services), Icons.Rounded.Category),
        BottomTab("more", stringResource(R.string.nav_more), Icons.Rounded.Tune),
    )

    val navController = rememberNavController()
    val navigateToTab: (String) -> Unit = remember(navController) {
        { route ->
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
    val backStack by navController.currentBackStackEntryAsState()
    val currentDestination = backStack?.destination
    val selectedRoute = tabs.firstOrNull { dest ->
        currentDestination?.hierarchy?.any { it.route == dest.route } == true
    }?.route
    val showBottomBar = selectedRoute != null
    val content by viewModel.content.collectAsStateWithLifecycle()
    val portalInfo by viewModel.portalInfo.collectAsStateWithLifecycle()
    val opening by viewModel.opening.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val openBrowser: (String) -> Unit = remember(context, viewModel) {
        { url -> viewModel.openUrl(context, url) }
    }
    val openPortalInfo: (PortalInfo) -> Unit = remember(navController, viewModel) {
        { info ->
            viewModel.preparePortalInfo(info)
            navController.navigate("service-info")
        }
    }

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
                Column(modifier = Modifier.fillMaxWidth()) {
                    BannerAd()
                    AnimatedBottomBar(
                        tabs = tabs,
                        selectedRoute = selectedRoute,
                        onTabSelected = navigateToTab,
                    )
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
                enterTransition = {
                    fadeIn(animationSpec = tween(220)) + slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(280),
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(180))
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(220)) + slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(280),
                    )
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(180))
                },
            ) {
                composable(route = "home") {
                    HomeScreen(
                        guides = content.guides,
                        provinces = content.provinces,
                        onProvinceClick = { id -> navController.navigate("province/$id") },
                        onOpenProvinces = { navigateToTab("provinces") },
                        onOpenServices = { navigateToTab("services") },
                        onOpenLicence = { navigateToTab("licence") },
                        onOpenSitePage = openBrowser,
                        onOpenPortalInfo = openPortalInfo,
                    )
                }
                composable(route = "licence") {
                    LicenceScreen(
                        licences = content.licences,
                        onOpenSitePage = openBrowser,
                        onOpenPortalInfo = openPortalInfo,
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
                        onOpenSitePage = openBrowser,
                        onOpenPortalInfo = openPortalInfo,
                    )
                }
                composable(route = "more") {
                    MoreScreen(
                        onOpenSitePage = openBrowser,
                        onOpenSources = { navController.navigate("sources") },
                        onRefreshConfig = viewModel::refreshConfig,
                    )
                }
                composable(route = "sources") {
                    SourcesScreen(
                        sources = AppContent.governmentSources,
                        onBack = { navController.popBackStack() },
                        onOpenSourceInfo = openPortalInfo,
                    )
                }
                composable(route = "service-info") {
                    ServiceInfoScreen(
                        info = portalInfo,
                        onBack = {
                            navController.popBackStack()
                            viewModel.clearPortalInfo()
                        },
                        onOpenOfficialWebsite = openBrowser,
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
                        onOpenSitePage = openBrowser,
                        onOpenPortalInfo = openPortalInfo,
                    )
                }
            }

            if (opening) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Sea,
                )
            }
        }
    }
}
