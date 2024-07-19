package com.uiel.dopaminedetox

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Calendar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val context = LocalContext.current

    var isAccessPermissionGranted by remember { mutableStateOf(hasAccessibilityPermission(context)) }
    var showAccessibilityDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }

    var usageStats by remember { mutableLongStateOf(0L) }
    var isTimePermissionGranted by remember {
        mutableStateOf(false)
    }
    var hour = 0L
    var minute = 0L

    LaunchedEffect(showTimeDialog) {
        Log.d("TEST1","1")
        if (!hasUsageStatsPermission(context)) {
            showTimeDialog = true
        } else {
            showTimeDialog = false
            isTimePermissionGranted = true
            usageStats = getDailyUsageTime(context)
        }
    }

    LaunchedEffect(showAccessibilityDialog) {
        if (!isAccessPermissionGranted) {
            showAccessibilityDialog = true
        } else {
            showAccessibilityDialog = false
        }
    }

    TimePermissionDialog(
        showDialog = showTimeDialog,
        onDismiss = { showTimeDialog = false },
        onConfirm = {
            showTimeDialog = false
            requestUsageStatsPermission(context)
        }
    )

    AccessibilityPermissionDialog(
        showDialog = showAccessibilityDialog,
        onDismiss = { showAccessibilityDialog = false },
        onConfirm = {
            showAccessibilityDialog = false
            requestAccessibilityPermission(context)
        },
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        hour = (usageStats / (1000 * 60 * 60)) % 24
        minute = (usageStats / (1000 * 60)) % 60
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "핸드폰 사용시간:",
            color = Color.Gray,
            fontSize = 20.sp,
        )
        Text(
            text = "$hour 시간 $minute 분",
            color = Color.Black,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonColors(
                containerColor = Color(0xFF7187C2),
                contentColor = Color.White,
                disabledContentColor = Color.White,
                disabledContainerColor = Color(0xFF7187C2),
            ),
            onClick = { navController.navigate("select") },
        ) {
            Text(
                modifier = Modifier.padding(vertical = 14.dp),
                text = "디톡스 어플 선택",
                fontSize = 20.sp,
            )
        }
    }
}

fun hasUsageStatsPermission(context: Context): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
    val mode = appOps.checkOpNoThrow(
        android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(),
        context.packageName
    )
    return mode == android.app.AppOpsManager.MODE_ALLOWED
}

fun requestUsageStatsPermission(context: Context) {
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

fun getDailyUsageTime(context: Context): Long {
    val usageStatsManager =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    val calendar = Calendar.getInstance()
    val endTime = calendar.timeInMillis
    calendar.add(Calendar.DAY_OF_YEAR, -1)
    val startTime = calendar.timeInMillis

    val queryUsageStats =
        usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)

    var totalUsageTime: Long = 0
    for (usageStats in queryUsageStats) {
        totalUsageTime += usageStats.totalTimeInForeground
    }

    return totalUsageTime
}

fun hasAccessibilityPermission(context: Context): Boolean {
    val enabledServices = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    )
    return enabledServices != null && enabledServices.contains(context.packageName)
}

fun requestAccessibilityPermission(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

@Composable
fun AccessibilityPermissionDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "접근 제어 권한 허용") },
            text = { Text(text = "앱 접근을 관리하기 위해 권한을 허용해주세요") },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text("설정하러 가기")
                }
            },
        )
    }
}

@Composable
fun TimePermissionDialog(showDialog: Boolean, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "접근 권한 허용") },
            text = { Text(text = "핸드폰 사용시간 확인을 위해 권한을 허용해주세요") },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text("설정하러 가기")
                }
            },
        )
    }
}
