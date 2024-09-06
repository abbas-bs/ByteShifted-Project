package com.example.byteshiftedproject

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db= Firebase.firestore


    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> = _userEmail

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber

    private val _userAddress = MutableLiveData<String>()
    val userAddress: LiveData<String> = _userAddress

    // Validation functions
    fun isValidName(name: String): Boolean {
        return name.isNotBlank()
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$"
        return password.matches(Regex(passwordPattern))
    }

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unaunthenticated
        } else {
            _authState.value = AuthState.Authenticated
            fetchUserDetails()
        }
    }

    fun fetchUserDetails() {
        val user = auth.currentUser
        if (user != null) {
            db.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val firstName = document.getString("firstName") ?: "Unknown"
                        val lastName = document.getString("lastName") ?: "User"
                        _userName.value = "$firstName $lastName"
                        _userEmail.value = document.getString("email") ?: "No Email"
                        _phoneNumber.value = document.getString("phone_number") ?: ""
                        _userAddress.value = document.getString("address") ?: ""

                    } else {
                        _userName.value = "Unknown User"
                        _userEmail.value = "No Email"
                        _phoneNumber.value = ""
                        _userAddress.value = ""
                    }
                }
                .addOnFailureListener { exception ->
                    _userName.value = "Unknown User"
                    _userEmail.value = "No Email"
                    _phoneNumber.value = ""
                    _userAddress.value = ""
                }
        }
    }


    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and Password can't be empty.")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                    fetchUserDetails()
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something Went Wrong")
                }
            }
    }

    fun signup(firstName: String, lastName: String, email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and Password can't be empty.")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        updateUserProfile(user, firstName, lastName)
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something Went Wrong")
                }
            }
    }

    private fun updateUserProfile(user: FirebaseUser, firstName: String, lastName: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName("$firstName $lastName")
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Save user data to Firestore after profile update
                    saveUserDataToFirestore(user, firstName, lastName)
                    fetchUserDetails()
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Failed to update profile")
                }
            }
    }

    private fun saveUserDataToFirestore(user: FirebaseUser, firstName: String, lastName: String) {
        val userData = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to user.email,
            "phone_number" to "", // Initialized as empty
            "address" to "" // Initialized as empty
        )

        db.collection("users").document(user.uid) // Use user.uid as document ID
            .set(userData)
            .addOnSuccessListener {
                Log.d("AuthViewModel", "User data saved to Firestore")
            }
            .addOnFailureListener { e ->
                Log.w("AuthViewModel", "Error saving user data to Firestore", e)
            }
    }

    fun updateUserProfileDetails(phone: String, address: String) {
        val user = auth.currentUser
        if (user != null) {
            val userData = hashMapOf(
                "phone_number" to phone,
                "address" to address
            )as Map<String, Any>

            db.collection("users").document(user.uid)
                .update(userData)
                .addOnSuccessListener {
                    _phoneNumber.value = phone
                    _userAddress.value = address
                    //Here I want to show user that Changes has been saved
                }
                .addOnFailureListener { e ->
                    Log.w("AuthViewModel", "Error updating user profile", e)
                }
        }
    }


    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unaunthenticated
        _userName.value = "Unknown User"
        _userEmail.value = "No Email"
    }
}


sealed class AuthState{
    object Authenticated : AuthState()
    object Unaunthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}