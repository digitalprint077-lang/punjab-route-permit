package com.pakexciseinfo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pakexciseinfo.app.ui.home.HomeScreen
import com.pakexciseinfo.app.ui.more.MoreScreen
import com.pakexciseinfo.app.ui.provinces.ProvinceDetailScreen
import com.pakexciseinfo.app.ui.provinces.ProvincesScreen
import com.pakexciseinfo.app.ui.services.ServicesScreen
import com.pakexciseinfo.app.ui.theme.PakExciseTheme
import com.pakexciseinfo.app.util.LinkOpener

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PakExciseTheme {
                PakExciseApp(openUrl = { url -> LinkOpener.open(this, url) })
            }
        }
    }
}

private data class TopDest(
    val route: String,
    val labelRes: Int,
    val icon: ImageVector,
)

private val HomeDest = TopDest("home", R.string.nav_home, Icons.Rounded.Home)
private val ProvincesDest = TopDest("provinces", R.string.nav_provinces, Icons.Rounded.Map)
private val ServicesDest = TopDest("services", R.string.nav_services, Icons.Rounded.Apps)
private val MoreDest = TopDest("more", R.string.nav_more, Icons.Rounded.MoreHoriz)

private val topDestinations = listOf(HomeDest, ProvincesDest, ServicesDest, MoreDest)

@Composable
private fun PakExciseApp(openUrl: (String) -> Unit) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentDestination = backStack?.destination
    val showBottomBar = topDestinations.any { dest ->
        currentDestination?.hierarchy?.any { it.route == dest.route } == true
    }

    Scaffold(
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
        NavHost(
            navController = navController,
            startDestination = HomeDest.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = HomeDest.route) {
                HomeScreen(
                    onProvinceClick = { id -> navController.navigate("province/$id") },
                    onOpenProvinces = {
                        navController.navigate(ProvincesDest.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onOpenServices = {
                        navController.navigate(ServicesDest.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onOpenOfficial = openUrl,
                )
            }
            composable(route = ProvincesDest.route) {
                ProvincesScreen(
                    onProvinceClick = { id -> navController.navigate("province/$id") },
                )
            }
            composable(route = ServicesDest.route) {
                ServicesScreen(
                    onOpenOfficial = openUrl,
                    onOpenGuide = openUrl,
                )
            }
            composable(route = MoreDest.route) {
                MoreScreen(onOpenUrl = openUrl)
            }
            composable(
                route = "province/{provinceId}",
                arguments = listOf(
                    navArgument(name = "provinceId") { type = NavType.StringType },
                ),
            ) { entry ->
                val id = entry.arguments?.getString("provinceId").orEmpty()
                ProvinceDetailScreen(
                    provinceId = id,
                    onBack = { navController.popBackStack() },
                    onOpenOfficial = openUrl,
                    onOpenGuide = openUrl,
                )
            }
        }
    }
}
