package com.example.byteshiftedproject.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.byteshiftedproject.AuthState
import com.example.byteshiftedproject.AuthViewModel
import com.example.byteshiftedproject.Routes

@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val userName by authViewModel.userName.observeAsState("Unknown User")
    val userEmail by authViewModel.userEmail.observeAsState("No Email")

    val authState by authViewModel.authState.observeAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Unaunthenticated -> navController.navigate(Routes.login) {
                popUpTo(Routes.home) { inclusive = true }
            }
            is AuthState.Authenticated -> Unit
            is AuthState.Loading -> Unit
            is AuthState.Error -> Unit
            null -> Unit
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = "Home Page", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Welcome, $userName", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Email: $userEmail", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {
            navController.navigate(Routes.profile)
        }) {
            Text(text = "Click Here to View Profile", fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {
            authViewModel.signout()
            navController.navigate(Routes.login) {
                popUpTo(Routes.home) { inclusive = true }
            }
        }) {
            Text(text = "Sign Out", fontSize = 28.sp)
        }

    }
}
