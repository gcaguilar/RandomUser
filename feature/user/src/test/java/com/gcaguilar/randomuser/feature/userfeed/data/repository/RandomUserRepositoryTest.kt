package com.gcaguilar.randomuser.feature.userfeed.data.repository

import com.gcaguilar.randomuser.feature.userfeed.data.api.UserRemoteDataSource
import com.gcaguilar.randomuser.feature.userfeed.mother.firstPageList
import com.gcaguilar.randomuser.userlocalstorageapi.UserLocalDataSource
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class RandomUserRepositoryTest {
    private val localDataSource: UserLocalDataSource = mockk(relaxed = true)
    private val remoteDataSource: UserRemoteDataSource = mockk(relaxed = true)
    private lateinit var userRepository: RandomUserRepository

    @Before
    fun setUp() {
        userRepository = RandomUserRepository(localDataSource, remoteDataSource)
    }

    @Test
    fun `Given a successful page request when data is inserted then data is inserted in database`() =
        runTest {
            coEvery { remoteDataSource.getUsers(any(), any()) } returns Result.success(firstPageList)
            coEvery { localDataSource.getDeletedUsers() } returns emptyList()

            userRepository.getPage(1, "some feed")

            coVerify { localDataSource.insertAll(firstPageList) }
        }


    @Test
    fun `Given failed page request when data is arrived then no data is inserted`() =
        runTest {
            coEvery { remoteDataSource.getUsers(any(), any()) } returns Result.failure(Throwable())
            coEvery { localDataSource.getDeletedUsers() } returns emptyList()

            userRepository.getPage(1, "some feed")

            coVerify(exactly = 0) { localDataSource.insertAll(any()) }
        }

    @Test
    fun `Given that a user is deleted, when the deletion is performed, then delete operation should executed`() =
        runTest {
            coEvery { localDataSource.getDeletedUsers() } returns listOf(firstPageList[0].uuid)

            userRepository.deleteUser(firstPageList[0].uuid)

            coVerify { localDataSource.deleteUser(firstPageList[0].uuid) }
            coVerify { localDataSource.insertInDelete(firstPageList[0].uuid) }
        }

    @Test
    fun `Given a successful page request when some user is in delete users then only filtered users should be inserted`() =
        runTest {
            val filteredList = firstPageList.filter { it.uuid != firstPageList[0].uuid }
            coEvery { remoteDataSource.getUsers(any(), any()) } returns Result.success(firstPageList)
            coEvery { localDataSource.getDeletedUsers() } returns listOf(firstPageList[0].uuid)

            userRepository.getPage(1, "some feed")

            val usersCaptor = slot<List<UserModelDetailed>>()
            coVerify { localDataSource.insertAll(capture(usersCaptor)) }
            val insertedUsers = usersCaptor.captured
            assertEquals(filteredList.size, insertedUsers.size)
            assertFalse(insertedUsers.any { it.uuid == firstPageList[0].uuid })
        }
}