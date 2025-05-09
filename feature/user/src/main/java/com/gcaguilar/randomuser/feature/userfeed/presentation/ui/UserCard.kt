package com.gcaguilar.randomuser.feature.userfeed.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun UserCard(
    name: String,
    email: String,
    picture: String,
    phone: String,
    onRemoveUser: () -> Unit,
    onClickUser: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable {
            onClickUser()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                modifier = Modifier.size(64.dp),
                model = picture,
                contentDescription = null,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(name)
                Text(email)
                Text(phone)
            }
            IconButton(
                modifier = Modifier.testTag("Delete${name}"),
                onClick = onRemoveUser
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete user",
                )
            }
        }
    }
}
