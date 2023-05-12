package com.girlify.dolarapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.girlify.dolarapp.dolar.ui.dollar.DollarScreen
import com.girlify.dolarapp.dolar.ui.dollar.DollarViewModel
import com.girlify.dolarapp.dolar.ui.splash.SplashViewModel
import com.girlify.dolarapp.ui.theme.DolarAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val dollarViewModel: DollarViewModel by viewModels()
    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition{splashViewModel.isLoading.value}

        setContent {
            DolarAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DollarScreen(dollarViewModel)
                }
            }
        }
    }
}
