package com.example.bookreader.ui.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun makeUserWithEmailAndPassword(email: String, password: String, home: () -> Unit) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            loadingState.value = LoadingState.SUCCESS
                            val displayName = task.result.user?.email?.split("@")?.get(0)
                            makeUser(displayName)
                            home()
                        } else {
                            loadingState.value = LoadingState.FAILED
                            task.exception?.printStackTrace()
                        }
                        _loading.value = false
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            loadingState.value = LoadingState.SUCCESS
                            home()
                        } else {
                            loadingState.value = LoadingState.FAILED
                        }
                        _loading.value = false
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun makeUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val user = MUser(
            id = null,
            userId = userId!!,
            displayName = displayName!!,
            image = "",
            quote = "\"Life is great, $displayName\"",
            profession = "Android Dev",
        )

        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(userId)
            .set(user)

    }

}