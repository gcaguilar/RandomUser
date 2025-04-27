package com.gcaguilar.randomuser.feature.userdetail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gcaguilar.randomuser.ui.theme.RandomUserTheme
import com.gcaguilar.randomuser.ui.theme.ui.Error
import com.gcaguilar.randomuser.ui.theme.ui.Loading
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserDetailScreen(
    userId: String,
    modifier: Modifier = Modifier,
    viewModel: UserDetailViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handle(UserDetailIntent.GetUser(userId))
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when (state.value.screenState) {
            State.Loading -> Loading(modifier = Modifier.fillMaxSize())
            State.Idle -> UserDetail(
                modifier = Modifier.fillMaxSize()
                    .weight(1f),
                name = state.value.name,
                gender = state.value.gender,
                street = state.value.street,
                city = state.value.city,
                state = state.value.state,
                email = state.value.email,
                picture = state.value.picture
            )

            State.Error -> Error(
                onRetry = {
                    viewModel.handle(UserDetailIntent.GetUser(userId))
                }
            )
        }
    }
}

@Composable
private fun UserDetail(
    name: String,
    gender: String,
    street: String,
    city: String,
    state: String,
    email: String,
    picture: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(
                top = 32.dp,
                start = 16.dp,
                end = 16.dp
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier
                .size(64.dp),
            model = picture,
            contentDescription = ""
        )
        Text(name)
        Text(gender)
        Text(street)
        Text(city)
        Text(state)
        Text(email)
    }
}

@Preview
@Composable
fun UserDetailPreview() {
    RandomUserTheme {
        UserDetail(
            name = "Homer J. Simpson",
            gender = "Male",
            street = "742 Evergreen Terrace",
            city = "Springfield",
            state = "Alaska",
            email = "chunkylover53@aol.com",
            picture = "https://i.pinimg.com/736x/8b/36/59/8b36592056842e0d8ced0404d4222538.jpg"
        )
    }
}