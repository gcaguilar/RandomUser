package com.gcaguilar.randomuser.feature.user.presentation

import app.cash.turbine.test
import com.gcaguilar.randomuser.feature.user.domain.DeleteUser
import com.gcaguilar.randomuser.feature.user.domain.GetUsers
import com.gcaguilar.randomuser.feature.user.domain.RequestNextPage
import com.gcaguilar.randomuser.feature.user.mother.firstPageList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class FeedRandomUserViewModelTest {
    private val getUsers: GetUsers = mockk(relaxed = true)
    private val deleteUser: DeleteUser = mockk(relaxed = true)
    private val requestNextPage: RequestNextPage = mockk(relaxed = true)
    private lateinit var viewModel: FeedRandomUserViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = FeedRandomUserViewModel(getUsers, deleteUser, requestNextPage)
    }


    @Test
    fun `Given that a user clears the text in the search box, when the action is performed, then a new correct state is emitted`() =
        runTest {
            val expected = UIState(
                users = firstPageList.toUserModel(),
                seed = "",
                page = 1,
                state = State.Idle,
                searchText = "",
                hasErrorRequestingMoreUsers = false,
                isRequestingMoreItems = false
            )
            coEvery { getUsers(any()) } returns flowOf(firstPageList)
            viewModel.handle(FeedUserIntent.StartObserving)

            viewModel.uiState.test {
                skipItems(1)
                viewModel.handle(FeedUserIntent.TextChanged("nicole"))
                assertEquals(firstPageList.toUserModel().first(), awaitItem().users.first())

                viewModel.handle(FeedUserIntent.TextChanged(""))
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
                page = 1,
                state = State.Idle,
                searchText = "nicole"
            )
            coEvery { getUsers(any()) } returns flowOf(listOf(firstPageList[0]))
            viewModel.handle(FeedUserIntent.StartObserving)
            viewModel.handle(FeedUserIntent.TextChanged("nicole"))

            viewModel.uiState.test {
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
                page = 1,
                state = State.Idle,
                searchText = ""
            )

            coEvery { getUsers(any()) } returns flowOf(firstPageList)
            coEvery { getUsers(any()) } returns flowOf(
                firstPageList.subList(1, firstPageList.size)
            )
            coEvery { deleteUser(firstPageList[0].uuid) } returns Unit
            viewModel.handle(FeedUserIntent.StartObserving)

            viewModel.uiState.test {
                viewModel.handle(FeedUserIntent.DeleteUser(firstPageList[0].uuid))

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

//    @Test
//    fun `Given that a user request more user, when the action is performed, then a new correct state is emitted`() =
//        runTest {
//            val expected = UIState(
//                users = (firstPageList.plus(secondPage)).toUserModel(),
//                seed = "",
//                page = 2,
//                state = State.Idle,
//                searchText = ""
//            )
//
//            coEvery { getUsers(any()) } returns flowOf(firstPageList.plus(secondPage))
//            viewModel.handle(FeedUserIntent.StartObserving)
//            advanceUntilIdle()
//            viewModel.handle(FeedUserIntent.RequestMoreUsers)
//            advanceUntilIdle()
//            viewModel.handle(FeedUserIntent.RequestMoreUsers)
//            advanceUntilIdle()
//
//            viewModel.uiState.test {
//
//                with(awaitItem()) {
//                    assertEquals(expected.users, this.users)
//                    assertEquals(expected.page, this.page)
//                    assertEquals(expected.state, this.state)
//                    assertEquals(expected.searchText, this.searchText)
//                    assertEquals(expected.isRequestingMoreItems, this.isRequestingMoreItems)
//                    assertEquals(
//                        expected.hasErrorRequestingMoreUsers,
//                        this.hasErrorRequestingMoreUsers
//                    )
//                }
//            }
//        }
}