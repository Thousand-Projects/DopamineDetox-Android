package com.uiel.dopaminedetox

import android.content.pm.PackageManager
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val context = LocalContext.current
    var isPermissionGranted by remember { mutableStateOf(hasAccessibilityPermission(context)) }
    var selectedAppPackage by remember { mutableStateOf(setOf<String>()) }
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    LaunchedEffect(Unit) {
        selectedAppPackage = sharedPreferences.getStringSet("selected_app_packages", emptySet()) ?: emptySet()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back",
                        )
                    }
                }
            )
        }
    ) { paddingValue->
        Column(modifier = modifier
            .padding(
                paddingValues = paddingValue,
            )
        ) {
            if (!isPermissionGranted) {
                Text(text = "Accessibility permission is required", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { requestAccessibilityPermission(context) }) {
                    Text(text = "Grant Permission")
                }
            } else {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = "디톡스 할 앱을 선택하세요",
                    fontSize = 18.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                AppList(selectedAppPackage) { packageName ->
                    val newSelectedPackages = selectedAppPackage.toMutableSet().apply {
                        if (contains(packageName)) remove(packageName) else add(packageName)
                    }
                    selectedAppPackage = newSelectedPackages
                    sharedPreferences.edit {
                        putStringSet("selected_app_packages", newSelectedPackages)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

}

@Composable
fun AppList(
    selectedAppPackages: Set<String>,
    onAppSelected: (String) -> Unit,
) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val apps = getInstalledApps(packageManager)

    LazyColumn {
        items(apps) { app ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAppSelected(app.packageName) }
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    bitmap = app.appIcon.toBitmap().asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = app.appName, fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))
                Checkbox(
                    checked = selectedAppPackages.contains(app.packageName),
                    onCheckedChange = { onAppSelected(app.packageName) },
                )
            }
        }
    }
}

fun getInstalledApps(packageManager: PackageManager): List<AppInfo> {
    val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        .filter { packageManager.getLaunchIntentForPackage(it.packageName) != null}
    return apps.map { app ->
        AppInfo(
            packageName = app.packageName,
            appName = packageManager.getApplicationLabel(app).toString(),
            appIcon = packageManager.getApplicationIcon(app)
        )
    }
}
