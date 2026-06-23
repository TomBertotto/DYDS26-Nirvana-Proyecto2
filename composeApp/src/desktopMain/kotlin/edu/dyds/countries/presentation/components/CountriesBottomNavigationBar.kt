@file:Suppress("FunctionName")

package edu.dyds.countries.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

const val EXPLORE_NAV_INDEX = 0
const val COMPARE_NAV_INDEX = 1

private data class BottomNavItem(val label: String, val icon: ImageVector, val index: Int)

private val bottomNavItems = listOf(
    BottomNavItem("Explore", Icons.Filled.Search, EXPLORE_NAV_INDEX),
    BottomNavItem("Compare", Icons.Filled.Star, COMPARE_NAV_INDEX)
)

@Composable
fun CountriesBottomNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(containerColor = Color.White) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = selectedIndex == item.index,
                onClick = { onItemSelected(item.index) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppColors.PrimaryBlue,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = AppColors.PrimaryBlue,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}
