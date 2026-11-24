package ca.gbc.comp3074.sivaganesan_eneiyavan.lab9.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ca.gbc.comp3074.sivaganesan_eneiyavan.lab9.model.User
import ca.gbc.comp3074.sivaganesan_eneiyavan.lab9.network.RetrofitInstance

sealed interface UsersUiState {
    data object Loading : UsersUiState
    data class Success(val users: List<User>) : UsersUiState
    data class Error(val message: String) : UsersUiState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    onContactClick: (Int) -> Unit
) {
    var uiState by remember {
        mutableStateOf<UsersUiState>(UsersUiState.Loading)
    }

    LaunchedEffect(Unit) {
        uiState = try {
            UsersUiState.Success(RetrofitInstance.api.getUsers())
        } catch (e: Exception) {
            UsersUiState.Error(e.message ?: "Unable to load contacts.")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Contacts") })
        }
    ) { paddingValues ->
        when (val state = uiState) {
            UsersUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UsersUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${state.message}")
                }
            }

            is UsersUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(
                        items = state.users,
                        key = { it.id }
                    ) { user ->
                        ContactRow(
                            user = user,
                            onClick = { onContactClick(user.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContactRow(
    user: User,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = "☘ ${user.name}",
                fontWeight = FontWeight.SemiBold
            )
        },
        supportingContent = {
            Text(user.email)
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    )
    HorizontalDivider()
}
