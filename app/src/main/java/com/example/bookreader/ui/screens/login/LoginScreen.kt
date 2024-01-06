package com.example.bookreader.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bookreader.components.ReaderLogo
import com.example.bookreader.components.UserForm
import com.example.bookreader.navigation.ReaderScreens
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel = viewModel()) {

    val showLoginForm = rememberSaveable { mutableStateOf(true) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ReaderLogo()
            if (showLoginForm.value) {
                UserForm(
                    loading = false,
                    isCreateAccount = false,
                    onDone = { email, password ->
                        viewModel.signInWithEmailAndPassword(email, password){
                            navController.navigate(ReaderScreens.HomeScreen.name)
                        }
                    }
                )
            } else {
                UserForm(
                    loading = false,
                    isCreateAccount = true,
                    onDone = { email, password ->
                        viewModel.makeUserWithEmailAndPassword(email, password){
                            navController.navigate(ReaderScreens.HomeScreen.name)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text = if (showLoginForm.value) "Sign Up" else "Login"
                val color = if (showLoginForm.value) Color.Blue else Color.Green
                val alreadyUser = if (showLoginForm.value) "New User? " else "Already have an account? "
                Text(text = alreadyUser, color = color)
                Text(text = text, modifier = Modifier
                    .clickable {
                        showLoginForm.value = !showLoginForm.value
                    }
                    .padding(start = 5.dp)
                    .align(alignment = Alignment.CenterVertically))
            }
        }
    }
}


