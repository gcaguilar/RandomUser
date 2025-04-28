package com.gcaguilar.randomuser.feature.userdetail.presentation

import app.cash.turbine.test
import com.gcaguilar.randomuser.feature.userfeed.mother.firstPageList
import com.gcaguilar.randomuser.feature.userdetail.domain.GetUserDetail
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserDetailViewModelTest {
    private val getUserDetail: GetUserDetail = mockk(relaxed = true)
    private lateinit var viewModel: UserDetailViewModel

    @Before
    fun setUp() {
        viewModel = UserDetailViewModel(getUserDetail)
    }

    @Test
    fun `Given valid UUID when provided user info then emit state`() = runTest {
        coEvery { getUserDetail(firstPageList[0].uuid) } returns Result.success(firstPageList[0])

        viewModel.uiState.test {
            skipItems(1)

            viewModel.handle(UserDetailIntent.GetUser(firstPageList[0].uuid))
            advanceUntilIdle()

            with(awaitItem()) {
                assertEquals(State.Idle, this.screenState)
                assertEquals("${firstPageList[0].name} ${firstPageList[0].surname}", this.name)
                assertEquals(firstPageList[0].gender, this.gender)
                assertEquals(firstPageList[0].email, this.email)
                assertEquals(firstPageList[0].city, this.city)
                assertEquals(firstPageList[0].street, this.street)
                assertEquals(firstPageList[0].state, this.state)
            }
        }
    }

    @Test
    fun `Given invalid UUID when provided user info then emit state`() = runTest {
        coEvery { getUserDetail(firstPageList[0].uuid) } returns Result.failure(Throwable())

        viewModel.uiState.test {
            skipItems(1)

            viewModel.handle(UserDetailIntent.GetUser(firstPageList[0].uuid))
            advanceUntilIdle()

            with(awaitItem()) {
                assertEquals(State.Error, this.screenState)
                assertNotEquals("${firstPageList[0].name} ${firstPageList[0].surname}", this.name)
                assertNotEquals(firstPageList[0].gender, this.gender)
                assertNotEquals(firstPageList[0].email, this.email)
                assertNotEquals(firstPageList[0].city, this.city)
                assertNotEquals(firstPageList[0].street, this.street)
                assertNotEquals(firstPageList[0].state, this.state)
            }
        }
    }
}