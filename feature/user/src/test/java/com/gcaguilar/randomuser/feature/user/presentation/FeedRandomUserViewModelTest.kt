package com.gcaguilar.randomuser.feature.user.presentation

import app.cash.turbine.test
import com.gcaguilar.randomuser.feature.user.data.api.RandomUserApiClient
import com.gcaguilar.randomuser.feature.user.data.api.UserRemoteDataSource
import com.gcaguilar.randomuser.feature.user.data.repository.RandomUserRepository
import com.gcaguilar.randomuser.feature.user.domain.DeleteUser
import com.gcaguilar.randomuser.feature.user.domain.GetUsers
import com.gcaguilar.randomuser.feature.user.fake.FakeUserLocalDataSource
import com.gcaguilar.randomuser.feature.user.modules.networkTestModule
import com.gcaguilar.randomuser.feature.user.mother.firstPageList
import com.gcaguilar.randomuser.userlocalstorageapi.UserLocalDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import kotlin.test.assertEquals

class FeedRandomUserViewModelTest : KoinTest {
    private val getUser: GetUsers by inject()
    private val deleteUser: DeleteUser by inject()
    private lateinit var viewModel: FeedRandomUserViewModel

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                factory<UserLocalDataSource> { FakeUserLocalDataSource() }
                single<UserRemoteDataSource> { RandomUserApiClient(get()) }
                factory<RandomUserRepository> { RandomUserRepository(get(), get()) }
                factory<GetUsers> { GetUsers(get()) }
                factory<DeleteUser> { DeleteUser(get()) }
            },
            networkTestModule
        )
    }

    @Before
    fun setUp() {
        viewModel = FeedRandomUserViewModel(getUser, deleteUser)
    }

    @Test
    fun `Given that a user clears the text in the search box, when the action is performed, then a new correct state is emitted`() =
        runTest {
            val expected = UIState(
                users = firstPageList.toUserModel(),
                seed = "",
                page = 1,
                state = State.Idle,
                searchText = ""
            )

            viewModel.uiState.test {
                skipItems(1)
                viewModel.handle(FeedUserIntent.TextChanged("nicole"))
                skipItems(1)
                with(awaitItem()) {
                    assertEquals(listOf(expected.users[0]), this.users)
                    assertEquals("nicole", this.searchText)
                }

                viewModel.handle(FeedUserIntent.TextChanged(""))
                skipItems(1)
                with(awaitItem()) {
                    assertEquals(expected.users, this.users)
                    assertEquals(expected.page, this.page)
                    assertEquals(expected.state, this.state)
                    assertEquals(expected.searchText, this.searchText)
                }
            }
        }

    @Test
    fun `Given that a user introduce nicole in the search box, when the action is performed, then a new correct state is emitted`() =
        runTest {
            val expected = UIState(
                users = firstPageList.toUserModel(),
                seed = "",
                page = 1,
                state = State.Idle,
                searchText = "nicole"
            )

            viewModel.uiState.test {
                assertEquals(expected.users, awaitItem().users)
                viewModel.handle(FeedUserIntent.TextChanged("nicole"))

                skipItems(1)
                with(awaitItem()) {
                    assertEquals(listOf(expected.users[0]), this.users)
                    assertEquals(expected.page, this.page)
                    assertEquals(expected.state, this.state)
                    assertEquals(expected.searchText, this.searchText)
                }
            }
        }
}