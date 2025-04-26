package com.gcaguilar.randomuser.feature.user.presentation

import android.app.Application
import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gcaguilar.randomuser.MainCoroutineRule
import com.gcaguilar.randomuser.createRoborazziRule
import com.gcaguilar.randomuser.createScreenshotTestComposeRule
import com.gcaguilar.randomuser.feature.user.data.api.RandomUserApiClient
import com.gcaguilar.randomuser.feature.user.data.api.UserRemoteDataSource
import com.gcaguilar.randomuser.feature.user.data.repository.RandomUserRepository
import com.gcaguilar.randomuser.feature.user.domain.GetUsers
import com.gcaguilar.randomuser.feature.user.fake.FakeUserLocalDataSource
import com.gcaguilar.randomuser.feature.user.modules.networkTestModule
import com.gcaguilar.randomuser.ui.theme.RandomUserTheme
import com.gcaguilar.randomuser.userlocalstorageapi.UserLocalDataSource
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel5)
class RandomUserScreenTest : KoinTest {
    private val viewModel: FeedRandomUserViewModel by inject()

    @get:Rule(order = 1)
    val addActivityToRobolectricRule = object : TestWatcher() {
        override fun starting(description: Description?) {
            super.starting(description)
            val appContext: Application = ApplicationProvider.getApplicationContext()
            val activityInfo = ActivityInfo().apply {
                name = ComponentActivity::class.java.name
                packageName = appContext.packageName
            }
            shadowOf(appContext.packageManager).addOrUpdateActivity(activityInfo)
        }
    }

    @get:Rule(order = 2)
    val composeTestRule = createScreenshotTestComposeRule()

    @get:Rule(order = 3)
    val coroutineRule = MainCoroutineRule()

    @get:Rule(order = 4)
    val roborazziRule = createRoborazziRule(composeTestRule)

    @get:Rule(order = 5)
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                factory<UserLocalDataSource> { FakeUserLocalDataSource() }
                factory<UserRemoteDataSource> { RandomUserApiClient(get()) }
                factory<RandomUserRepository> { RandomUserRepository(get(), get()) }
                factory<GetUsers> { GetUsers(get()) }
                viewModelOf(::FeedRandomUserViewModel)
            },
            networkTestModule
        )
    }

    @Test
    fun `Given the screen is opened when the database contains data then the list of users is displayed`() =
        runTest {
            renderScreen()
        }

    @Test
    fun `Given that a user enters the screen with data and types something in the search box when the action is performed then the results are updated`() =
        runTest {
            renderScreen()

            composeTestRule
                .onNodeWithTag("searchField")
                .performTextInput("nicole")
        }

    @Test
    fun `Given that a user enters the screen with data and delete text in the search box when the action is performed then the results are updated`() =
        runTest {
            renderScreen()

            composeTestRule
                .onNodeWithTag("searchField")
                .performTextInput("nicole")

            composeTestRule
                .onNodeWithTag("searchField")
                .performTextClearance()
        }

    private fun renderScreen() {
        composeTestRule.setContent {
            RandomUserTheme {
                FeedRandomUserScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
