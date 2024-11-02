package com.example.mybakery.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mybakery.viewmodel.UserViewModel

@Composable
fun UserListScreen(viewModel: UserViewModel = hiltViewModel()) {
    val users by viewModel.users.collectAsState()

    Column {
        Text("User List")
        LazyColumn {
            items(users) { user ->
                Text("ID: ${user.id}, Name: ${user.name}, Email: ${user.email}")
            }
        }
    }
}