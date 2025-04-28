package com.gcaguilar.randomuser.feature.userfeed.domain

import app.cash.turbine.test
import com.gcaguilar.randomuser.feature.userfeed.data.repository.RandomUserRepository
import com.gcaguilar.randomuser.feature.userfeed.mother.firstPageList
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetUsersTest {
    private val userRepository: RandomUserRepository = mockk(relaxed = true)
    private lateinit var getUser: GetUsers


    @Before
    fun setUp() {
        getUser = GetUsers(userRepository)
    }

    @Test
    fun `Given that users are requested without any filter applied when the request is made then all available users are retrieved`() =
        runTest {
            every { userRepository.getUsers() } returns flowOf(firstPageList)

            getUser(MutableStateFlow("").asStateFlow()).test {
                assertEquals(firstPageList, awaitItem())
            }
        }

    @Test
    fun `Given that users are requested with nicole as filter applied when the request is made then only users with nicole in name, surname or email are retrieved`() =
        runTest {
            every { userRepository.getUsers() } returns flowOf(firstPageList)

            getUser(MutableStateFlow("nicole").asStateFlow()).test {
                assertEquals(listOf(firstPageList[0]), awaitItem())
            }
        }

    @Test
    fun `Given that users are requested with á as filter applied when the request is made then no users are retrieved`() =
        runTest {
            every { userRepository.getUsers() } returns flowOf(firstPageList)

            getUser(MutableStateFlow("á").asStateFlow()).test {
                assertEquals(emptyList(), awaitItem())
            }
        }
}