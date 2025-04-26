package com.gcaguilar.randomuser.feature.user.data.repository

import com.gcaguilar.randomuser.feature.user.data.api.RandomUserApiClient
import com.gcaguilar.randomuser.feature.user.data.api.UserRemoteDataSource
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
import kotlin.test.assertNull

class RandomUserRepositoryTest : KoinTest {
    private val localDataSource: UserLocalDataSource by inject()
    private val remoteDataSource: UserRemoteDataSource by inject()
    private lateinit var userRepository: RandomUserRepository

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single<UserLocalDataSource> { FakeUserLocalDataSource() }
                single<UserRemoteDataSource> { RandomUserApiClient(get()) }
            },
            networkTestModule
        )
    }

    @Before
    fun setUp() {
        userRepository = RandomUserRepository(localDataSource, remoteDataSource)
    }

    @Test
    fun `Given a successful page request when data is inserted then data is inserted in database`() =
        runTest {
            userRepository.getPage(0, "some feed")

            val insertedUser = (localDataSource as FakeUserLocalDataSource).lastInsertedUser
            assertEquals(firstPageList.last(), insertedUser)
        }

    @Test
    fun `Given failed page request when data is arrived then no data is inserted`() =
        runTest {
            userRepository.getPage(666, "some feed")

            val insertedUser = (localDataSource as FakeUserLocalDataSource).lastInsertedUser
            assertNull(insertedUser)
        }
}