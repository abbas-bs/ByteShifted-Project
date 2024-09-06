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
import androidx.compose.runtime.LaunchedEffect
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
    val phoneNumber by authViewModel.phoneNumber.observeAsState("")
    val userAddress by authViewModel.userAddress.observeAsState("")

    var updatedPhoneNumber by remember { mutableStateOf(phoneNumber) }
    var updatedUserAddress by remember { mutableStateOf(userAddress) }


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
            },
            readOnly = true
            )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = userEmail, onValueChange = {},
            label = {
                Text(text = "Email")
            },
            readOnly = true
            )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = updatedPhoneNumber, onValueChange = {updatedPhoneNumber = it},
            label = {
                Text(text = "Phone Number")
            })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = updatedUserAddress, onValueChange = {updatedUserAddress = it},
            label = {
                Text(text = "Address")
            })
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = {
            authViewModel.updateUserProfileDetails(updatedPhoneNumber, updatedUserAddress)
        }) {
            Text(text = "Save Changes")
        }
    }

}