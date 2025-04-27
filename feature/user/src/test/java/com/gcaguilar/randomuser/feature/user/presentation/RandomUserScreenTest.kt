package com.gcaguilar.randomuser.feature.user.presentation

import android.app.Application
import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gcaguilar.randomuser.MainCoroutineRule
import com.gcaguilar.randomuser.createRoborazziRule
import com.gcaguilar.randomuser.createScreenshotTestComposeRule
import com.gcaguilar.randomuser.feature.user.domain.DeleteUser
import com.gcaguilar.randomuser.feature.user.domain.GetUsers
import com.gcaguilar.randomuser.feature.user.domain.RequestNextPage
import com.gcaguilar.randomuser.feature.user.mother.firstPageList
import com.gcaguilar.randomuser.ui.theme.RandomUserTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
    qualifiers = RobolectricDeviceQualifiers.Pixel5,
    sdk = [28]
)
class RandomUserScreenTest {
    private val getUsers: GetUsers = mockk(relaxed = true)
    private val deleteUser: DeleteUser = mockk(relaxed = true)
    private val requestNextPage: RequestNextPage = mockk(relaxed = true)
    private lateinit var viewModel: FeedRandomUserViewModel

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
    val roborazziRule = createRoborazziRule(composeTestRule)

    @get:Rule(order = 4)
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        viewModel = FeedRandomUserViewModel(
            getUsers,
            deleteUser,
            requestNextPage
        )
    }

    @Test
    fun `Given the screen is opened when the database contains data then the list of users is displayed`() =
        runTest {
            coEvery { getUsers(any()) } returns flowOf(firstPageList)
            coEvery { requestNextPage(any(), any()) } returns Result.success(Unit)
            renderScreen()
            advanceUntilIdle()
        }

    @Test
    fun `Given that a user enters the screen with data and types something in the search box when the action is performed then the results are updated`() =
        runTest {
            val testFlow = MutableStateFlow(firstPageList)
            coEvery { getUsers(any()) } returns testFlow
            coEvery { requestNextPage(any(), any()) } returns Result.success(Unit)

            renderScreen()
            advanceUntilIdle()

            composeTestRule
                .onNodeWithTag("searchField")
                .performTextInput("nicole")
            testFlow.update { listOf(firstPageList[0]) }
            advanceUntilIdle()
        }

    @Test
    fun `Given that a user enters the screen with data and delete text in the search box when the action is performed then the results are updated`() =
        runTest {
            val testFlow = MutableStateFlow(firstPageList)
            coEvery { getUsers(any()) } returns testFlow
            coEvery { requestNextPage(any(), any()) } returns Result.success(Unit)

            renderScreen()
            advanceUntilIdle()

            composeTestRule
                .onNodeWithTag("searchField")
                .performTextInput("nicole")
            testFlow.update { listOf(firstPageList[0]) }
            advanceUntilIdle()

            composeTestRule
                .onNodeWithTag("searchField")
                .performTextClearance()
            testFlow.update { firstPageList }
            advanceUntilIdle()
        }

    @Test
    fun `Given that a user enters the screen with data and click on delete button of some user whGetUsersen the action is performed then the results are updated`() =
        runTest {
            val testFlow = MutableStateFlow(firstPageList)
            coEvery { getUsers(any()) } returns testFlow
            coEvery { requestNextPage(any(), any()) } returns Result.success(Unit)

            renderScreen()
            advanceUntilIdle()

            composeTestRule
                .onNodeWithTag("DeleteNicole Gomez")
                .performClick()
            testFlow.update { firstPageList.subList(1, firstPageList.size) }
            advanceUntilIdle()
        }

    private fun renderScreen() {
        composeTestRule.setContent {
            RandomUserTheme {
                FeedRandomUserScreen(
                    navController = rememberNavController(),
                    viewModel = viewModel
                )
            }
        }
    }
}
