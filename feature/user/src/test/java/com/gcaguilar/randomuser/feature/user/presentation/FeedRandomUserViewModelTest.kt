package com.gcaguilar.randomuser.feature.user.presentation

import app.cash.turbine.test
import com.gcaguilar.randomuser.MainCoroutineRule
import com.gcaguilar.randomuser.feature.user.domain.DeleteUser
import com.gcaguilar.randomuser.feature.user.domain.GetUsers
import com.gcaguilar.randomuser.feature.user.domain.RequestNextPage
import com.gcaguilar.randomuser.feature.user.mother.firstPageList
import com.gcaguilar.randomuser.feature.user.mother.secondPage
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class FeedRandomUserViewModelTest {
    private val getUsers: GetUsers = mockk(relaxed = true)
    private val deleteUser: DeleteUser = mockk(relaxed = true)
    private val requestNextPage: RequestNextPage = mockk(relaxed = true)
    private lateinit var viewModel: FeedRandomUserViewModel

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = FeedRandomUserViewModel(getUsers, deleteUser, requestNextPage)
    }

    @Test
    fun `Given a user who enters the screen without data, when they are prompted for more information, then a list should be emitted if the process is successful`() =
        runTest {
            val expected = UIState(
                users = firstPageList.toUserModel(),
                seed = "",
                page = 2,
                state = State.Idle,
                searchText = "",
                hasErrorRequestingMoreUsers = false,
                isRequestingMoreItems = false
            )
            val testFlow = MutableStateFlow(emptyList<UserModelDetailed>())
            coEvery { getUsers(any()) } returns testFlow
            coEvery { requestNextPage(any(), any()) } returns Result.success(Unit)

            viewModel.handle(FeedUserIntent.StartObserving)
            testFlow.update { emptyList() }
            advanceUntilIdle()

            viewModel.uiState.test {
                assertEquals(0, awaitItem().users.size)

                testFlow.update { firstPageList }
                advanceUntilIdle()

                with(awaitItem()) {
                    assertEquals(expected.users, this.users)
                    assertEquals(expected.page, this.page)
                    assertEquals(expected.state, this.state)
                    assertEquals(expected.searchText, this.searchText)
                    assertEquals(expected.isRequestingMoreItems, this.isRequestingMoreItems)
                    assertEquals(
                        expected.hasErrorRequestingMoreUsers,
                        this.hasErrorRequestingMoreUsers
                    )
                }
                coVerify(exactly = 1) { requestNextPage(any(), any()) }
            }
        }

    @Test
    fun `Given a user who enters the screen without data, when they are prompted for more information, then error should be emitted if the process fail`() =
        runTest {
            val expected = UIState(
                users = emptyList(),
                seed = "",
                page = 1,
                state = State.Error,
                searchText = "",
                hasErrorRequestingMoreUsers = true,
                isRequestingMoreItems = false
            )
            val testFlow = MutableStateFlow(emptyList<UserModelDetailed>())
            coEvery { getUsers(any()) } returns testFlow
            coEvery { requestNextPage(any(), any()) } returns Result.failure(Throwable())

            viewModel.handle(FeedUserIntent.StartObserving)
            testFlow.update { emptyList() }
            advanceUntilIdle()

            viewModel.uiState.test {
                with(awaitItem()) {
                    assertEquals(expected.users, this.users)
                    assertEquals(expected.page, this.page)
                    assertEquals(expected.state, this.state)
                    assertEquals(expected.searchText, this.searchText)
                    assertEquals(expected.isRequestingMoreItems, this.isRequestingMoreItems)
                    assertEquals(
                        expected.hasErrorRequestingMoreUsers,
                        this.hasErrorRequestingMoreUsers
                    )
                }
                coVerify(exactly = 1) { requestNextPage(any(), any()) }
            }
        }

    @Test
    fun `Given that a user clears the text in the search box, when the action is performed, then a new correct state is emitted`() =
        runTest {
            val expected = UIState(
                users = firstPageList.toUserModel(),
                seed = "",
                page = 2,
                state = State.Idle,
                searchText = "",
                hasErrorRequestingMoreUsers = false,
                isRequestingMoreItems = false
            )
            val testFlow = MutableStateFlow(emptyList<UserModelDetailed>())
            coEvery { getUsers(any()) } returns testFlow
            coEvery { requestNextPage(any(), any()) } returns Result.success(Unit)

            viewModel.handle(FeedUserIntent.StartObserving)
            testFlow.update { firstPageList }
            advanceUntilIdle()

            viewModel.uiState.test {
                skipItems(1)

                viewModel.handle(FeedUserIntent.TextChanged("nicole"))
                testFlow.update { listOf(firstPageList[0]) }
                advanceUntilIdle()
                skipItems(1)
                assertEquals(firstPageList.toUserModel().first(), awaitItem().users.first())

                viewModel.handle(FeedUserIntent.TextChanged(""))
                testFlow.update { firstPageList }
                advanceUntilIdle()
                skipItems(1)

                with(awaitItem()) {
                    assertEquals(expected.users, this.users)
                    assertEquals(expected.page, this.page)
                    assertEquals(expected.state, this.state)
                    assertEquals(expected.searchText, this.searchText)
                    assertEquals(expected.isRequestingMoreItems, this.isRequestingMoreItems)
                    assertEquals(
                        expected.hasErrorRequestingMoreUsers,
                        this.hasErrorRequestingMoreUsers
                    )
                }
            }
        }

    @Test
    fun `Given that a user introduce nicole in the search box, when the action is performed, then a new correct state is emitted`() =
        runTest {
            val expected = UIState(
                users = firstPageList.toUserModel(),
                seed = "",
                page = 2,
                state = State.Idle,
                searchText = "nicole"
            )
            val testFlow = MutableStateFlow(emptyList<UserModelDetailed>())
            coEvery { getUsers(any()) } returns testFlow
            coEvery { requestNextPage(any(), any()) } returns Result.success(Unit)

            viewModel.handle(FeedUserIntent.StartObserving)
            testFlow.update { firstPageList }
            advanceUntilIdle()

            viewModel.uiState.test {
                skipItems(1)
                viewModel.handle(FeedUserIntent.TextChanged("nicole"))
                testFlow.update { listOf(firstPageList[0]) }
                advanceUntilIdle()
                skipItems(1)

                with(awaitItem()) {
                    assertEquals(listOf(expected.users[0]), this.users)
                    assertEquals(expected.page, this.page)
                    assertEquals(expected.state, this.state)
                    assertEquals(expected.searchText, this.searchText)
                    assertEquals(expected.isRequestingMoreItems, this.isRequestingMoreItems)
                    assertEquals(
                        expected.hasErrorRequestingMoreUsers,
                        this.hasErrorRequestingMoreUsers
                    )
                }
            }
        }

    @Test
    fun `Given that a user click on delete button of nicole, when the action is performed, then a new correct state is emitted`() =
        runTest {
            val expected = UIState(
                users = firstPageList.subList(1, firstPageList.size).toUserModel(),
                seed = "",
                page = 2,
                state = State.Idle,
                searchText = ""
            )
            val testFlow = MutableStateFlow(emptyList<UserModelDetailed>())
            coEvery { getUsers(any()) } returns testFlow
            coEvery { requestNextPage(any(), any()) } returns Result.success(Unit)
            coEvery { deleteUser(firstPageList[0].uuid) } returns Unit

            viewModel.handle(FeedUserIntent.StartObserving)
            testFlow.update { firstPageList }
            advanceUntilIdle()

            viewModel.uiState.test {
                skipItems(1)

                viewModel.handle(FeedUserIntent.DeleteUser(firstPageList[0].uuid))
                testFlow.update { firstPageList.subList(1, firstPageList.size) }
                advanceUntilIdle()

                with(awaitItem()) {
                    assertEquals(expected.users, this.users)
                    assertEquals(expected.page, this.page)
                    assertEquals(expected.state, this.state)
                    assertEquals(expected.searchText, this.searchText)
                    assertEquals(expected.isRequestingMoreItems, this.isRequestingMoreItems)
                    assertEquals(
                        expected.hasErrorRequestingMoreUsers,
                        this.hasErrorRequestingMoreUsers
                    )
                }
            }
        }

    @Test
    fun `Given that a user request more user, when the action is performed, then a new correct state is emitted`() =
        runTest {
            val totalList = firstPageList.plus(secondPage)
            val expected = UIState(
                users = totalList.toUserModel(),
                seed = "",
                page = 3,
                state = State.Idle,
                searchText = ""
            )
            val testFlow = MutableStateFlow(emptyList<UserModelDetailed>())
            coEvery { getUsers(any()) } returns testFlow
            coEvery { requestNextPage(any(), any()) } returns Result.success(Unit)

            viewModel.handle(FeedUserIntent.StartObserving)
            testFlow.update { firstPageList }
            advanceUntilIdle()
            viewModel.handle(FeedUserIntent.RequestMoreUsers)
            testFlow.update { totalList }
            advanceUntilIdle()

            viewModel.uiState.test {
                with(awaitItem()) {
                    assertEquals(expected.users, this.users)
                    assertEquals(expected.page, this.page)
                    assertEquals(expected.state, this.state)
                    assertEquals(expected.searchText, this.searchText)
                    assertEquals(expected.isRequestingMoreItems, this.isRequestingMoreItems)
                    assertEquals(
                        expected.hasErrorRequestingMoreUsers,
                        this.hasErrorRequestingMoreUsers
                    )
                }
            }
        }
}