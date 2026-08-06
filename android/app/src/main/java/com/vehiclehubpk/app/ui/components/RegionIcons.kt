package com.vehiclehubpk.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apartment
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.LocationCity
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Park
import androidx.compose.ui.graphics.vector.ImageVector

/** Generic Material icons — never use government seals/emblems. */
object RegionIcons {
    fun forProvince(id: String): ImageVector = when (id) {
        "punjab" -> Icons.Rounded.DirectionsCar
        "sindh" -> Icons.Rounded.LocationCity
        "kpk" -> Icons.Rounded.Map
        "islamabad" -> Icons.Rounded.Apartment
        "balochistan" -> Icons.Rounded.Park
        "gilgit" -> Icons.Rounded.Map
        else -> Icons.Rounded.LocationCity
    }

    fun forLicence(id: String): ImageVector = when {
        id.contains("punjab") -> Icons.Rounded.Badge
        id.contains("sindh") -> Icons.Rounded.Badge
        id.contains("islamabad") -> Icons.Rounded.Badge
        id.contains("kpk") -> Icons.Rounded.Badge
        id.contains("balochistan") -> Icons.Rounded.Badge
        id.contains("gilgit") -> Icons.Rounded.Badge
        else -> Icons.Rounded.Badge
    }
}
