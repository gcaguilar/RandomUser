package com.gcaguilar.randomuser.feature.user.presentation

import androidx.test.runner.AndroidJUnit4
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
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel5)
class RandomUserScreenTest : KoinTest {
    private val viewModel: FeedRandomUserViewModel by inject()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val composeTestRule = createScreenshotTestComposeRule()

    @get:Rule
    val roborazziRule = createRoborazziRule(composeTestRule)

    @get:Rule
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
