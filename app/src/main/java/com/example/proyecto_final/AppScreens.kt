package com.example.proyecto_final

sealed class AppScreens(val route: String){
    object MainScreen: AppScreens("MainScreen")
    object SettingsScreen: AppScreens("SettingsScreen")
}
