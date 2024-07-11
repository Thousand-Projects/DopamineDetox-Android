package com.uiel.dopaminedetox

import android.app.ActivityManager
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.system.exitProcess

@Composable
fun ThinkScreen(
    modifier: Modifier,
    navController: NavController,
    packageName: String,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "\"현재에 집중하고 행동해라\"",
            fontSize = 28.sp,
            color = Color.Black,
        )
        Text(
            text = "-괴테-",
            fontSize = 20.sp,
            color = Color.Gray,
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonColors(
                containerColor = Color(0xFF73C271),
                contentColor = Color.White,
                disabledContentColor = Color.White,
                disabledContainerColor = Color(0xFF73C271),
            ),
            onClick = {
                killApp(
                    context = context,
                    packageName = packageName
                )
            },
        ) {
            Text(
                modifier = Modifier.padding(vertical = 14.dp),
                text = "성장하러 가기",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp,
                ),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonColors(
                containerColor = Color(0xFFC27171),
                contentColor = Color.White,
                disabledContentColor = Color.White,
                disabledContainerColor = Color(0xFFC27171),
            ),
            onClick = { exitProcess(0) }
        ) {
            Text(text = "인생 망치러 가기")
        }
    }
}

fun killApp(
    context: Context,
    packageName: String,
) {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    activityManager.killBackgroundProcesses(packageName)
    //exitProcess(0)
}
