package com.uiel.dopaminedetox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier

class BreathActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val packageName = intent.getStringExtra("PACKAGE_NAME") ?: ""

        setContent {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
            ) { innerPadding->
                DetoxNavHost(
                    modifier = Modifier.padding(innerPadding),
                    packageName = packageName,
                )
            }
        }
    }
}
