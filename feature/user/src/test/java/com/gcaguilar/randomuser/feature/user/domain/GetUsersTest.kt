package com.gcaguilar.randomuser.feature.user.domain

import app.cash.turbine.test
import com.gcaguilar.randomuser.feature.user.data.api.RandomUserApiClient
import com.gcaguilar.randomuser.feature.user.data.api.UserRemoteDataSource
import com.gcaguilar.randomuser.feature.user.data.repository.RandomUserRepository
import com.gcaguilar.randomuser.feature.user.fake.FakeUserLocalDataSource
import com.gcaguilar.randomuser.feature.user.modules.networkTestModule
import com.gcaguilar.randomuser.feature.user.mother.firstPageList
import com.gcaguilar.randomuser.userlocalstorageapi.UserLocalDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import kotlin.test.assertEquals

class GetUsersTest : KoinTest {
    private val userRepository: RandomUserRepository by inject()
    private lateinit var getUser: GetUsers

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single<UserLocalDataSource> { FakeUserLocalDataSource() }
                single<UserRemoteDataSource> { RandomUserApiClient(get()) }
                factory<RandomUserRepository> { RandomUserRepository(get(), get()) }
            },
            networkTestModule
        )
    }

    @Before
    fun setUp() {
        getUser = GetUsers(userRepository)
    }

    @Test
    fun `Given that users are requested without any filter applied when the request is made then all available users are retrieved`() =
        runTest {
            getUser(MutableStateFlow("").asStateFlow()).test {
                assertEquals(firstPageList, awaitItem())
            }
        }

    @Test
    fun `Given that users are requested with nicole as filter applied when the request is made then only users with nicole in name, surname or email are retrieved`() =
        runTest {
            getUser(MutableStateFlow("nicole").asStateFlow()).test {
                assertEquals(listOf(firstPageList[0]), awaitItem())
            }
        }

    @Test
    fun `Given that users are requested with á as filter applied when the request is made then no users are retrieved`() =
        runTest {
            getUser(MutableStateFlow("á").asStateFlow()).test {
                assertEquals(emptyList(), awaitItem())
            }
        }
}