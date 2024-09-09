package com.example.byteshiftedproject.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.byteshiftedproject.AuthViewModel

@Composable
fun ProfilePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    val userName by authViewModel.userName.observeAsState("Unknown User")
    val userEmail by authViewModel.userEmail.observeAsState("No Email")

    var phoneNumber by remember { mutableStateOf("") }
    var userAddress by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "This is Profile Page", fontSize = 30.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = userName, onValueChange = {},
            label = {
                Text(text = "User Name")
            })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = userEmail, onValueChange = {},
            label = {
                Text(text = "Email")
            })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = phoneNumber, onValueChange = {phoneNumber = it},
            label = {
                Text(text = "Phone Number")
            })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = userAddress, onValueChange = {userAddress = it},
            label = {
                Text(text = "Address")
            })
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            authViewModel.updateUserProfileDetails(phoneNumber, userAddress)
        }) {
            Text(text = "Save Changes")
        }
    }

}