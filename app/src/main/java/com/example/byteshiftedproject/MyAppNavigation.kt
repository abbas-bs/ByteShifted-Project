package com.example.byteshiftedproject

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.byteshiftedproject.pages.HomePage
import com.example.byteshiftedproject.pages.LoginPage
import com.example.byteshiftedproject.pages.SignupPage

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.login, builder = {
        composable(Routes.login){
            LoginPage(modifier, navController, authViewModel)
        }
        composable(Routes.signup){
            SignupPage(modifier, navController, authViewModel)
        }
        composable(Routes.home){
            HomePage(modifier, navController, authViewModel)

            navController.popBackStack(Routes.login, inclusive = true)
        }
    })
}