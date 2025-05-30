package com.gcaguilar.randomuser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.gcaguilar.randomuser.feature.navigation.Feed
import com.gcaguilar.randomuser.feature.navigation.UserDetail
import com.gcaguilar.randomuser.feature.userfeed.presentation.FeedRandomUserScreen
import com.gcaguilar.randomuser.feature.userdetail.presentation.UserDetailScreen
import com.gcaguilar.randomuser.ui.theme.RandomUserTheme
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }

            RandomUserTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (currentRoute != Feed.toString()) {
                            TopAppBar(
                                title = {},
                                navigationIcon = {
                                    if (navController.previousBackStackEntry != null) {
                                        IconButton(onClick = { navController.navigateUp() }) {
                                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "")
                                        }
                                    }
                                }
                            )
                        }
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Feed,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Feed> {
                            FeedRandomUserScreen(
                                onReceiveError = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Something went wrong!...")
                                    }
                                },
                                navController = navController
                            )
                        }

                        composable<UserDetail> { backStackEntry ->
                            val userDetail = backStackEntry.toRoute<UserDetail>()
                            UserDetailScreen(userDetail.uuid)
                        }
                    }
                }
            }
        }
    }
}
