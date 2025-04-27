package com.gcaguilar.randomuser.feature.userdetail.presentation

import android.app.Application
import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gcaguilar.randomuser.MainCoroutineRule
import com.gcaguilar.randomuser.createRoborazziRule
import com.gcaguilar.randomuser.createScreenshotTestComposeRule
import com.gcaguilar.randomuser.feature.user.mother.firstPageList
import com.gcaguilar.randomuser.feature.userdetail.domain.GetUserDetail
import com.gcaguilar.randomuser.ui.theme.RandomUserTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class UserDetailScreenshotTest {
    private val getUserDetail: GetUserDetail = mockk(relaxed = true)
    private lateinit var viewModel: UserDetailViewModel

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
        viewModel = UserDetailViewModel(
            getUserDetail
        )
    }

    @Test
    fun `Given the user enters the screen with a valid UUID, When the screen loads, Then the user's data is displayed`() =
        runTest {
            coEvery { getUserDetail(any()) } returns Result.success(firstPageList[0])

            renderScreen()
            viewModel.handle(UserDetailIntent.GetUser(firstPageList[0].uuid))
            advanceUntilIdle()
        }

    @Test
    fun `Given the user enters the screen with a invalid UUID, When the screen loads, Then the user's show error`() =
        runTest {
            coEvery { getUserDetail(any()) } returns Result.failure(Throwable())

            renderScreen()
            viewModel.handle(UserDetailIntent.GetUser(firstPageList[0].uuid))
            advanceUntilIdle()
        }

    private fun renderScreen() {
        composeTestRule.setContent {
            RandomUserTheme {
                UserDetailScreen(
                    userId = "",
                    viewModel = viewModel
                )
            }
        }
    }
}
