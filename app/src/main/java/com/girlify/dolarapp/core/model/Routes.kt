package com.girlify.dolarapp.core.model

sealed class Routes(val route: String){
    object SplashScreen:Routes("splashScreen")
    object Dollar:Routes("dollar")
}