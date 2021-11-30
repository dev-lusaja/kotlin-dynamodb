package com.crud.dynamodb.service

import com.crud.dynamodb.dto.UserDTO
import com.crud.dynamodb.repository.DynamoRepository
import com.crud.dynamodb.schema.UserDynamoSchema
import com.crud.dynamodb.schema.UserId
import kotlinx.coroutines.*
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*

@Service
class DynamoService(val repository: DynamoRepository) {
    suspend fun findByUuid(uuid: String): Response {
        val response = Response(null)
        try {
            coroutineScope {
                val data: Deferred<UserDynamoSchema> = async(Dispatchers.IO) {
                    println("findByUuid: I'm working in thread ${Thread.currentThread().name}")
                    repository.findByUuid(uuid)
                }
                response.data = data.await()
            }
        } catch (e: Exception) {
            response.error = true
            response.message = e.message.toString()
        }
        return response
    }

    suspend fun findByName(name: String): Response {
        val response = Response(null)
        try {
            coroutineScope {
                val data: Deferred<List<UserDynamoSchema>> = async(Dispatchers.IO) {
                    println("findByName: I'm working in thread ${Thread.currentThread().name}")
                    repository.findByName(name)
                }
                response.data = data.await()
            }
        } catch (e: EmptyResultDataAccessException) {
            response.error = true
            response.message = e.message.toString()
        }
        return response
    }

    suspend fun findAll(): Response {
        val response = Response(null)
        try {
            coroutineScope {
                val data = async(Dispatchers.IO) {
                    println("findAll: I'm working in thread ${Thread.currentThread().name}")
                    repository.findAll()
                }
                response.data = data.await()
            }
        } catch (e: Exception) {
            response.error = true
            response.message = e.message.toString()
        }
        return response
    }

    suspend fun save(user: UserDTO): Response {
        val response = Response(null)
        try {
            val id: String = UUID.randomUUID().toString()
            val formatDate = SimpleDateFormat("dd/MM/yyyy")
            val createAt = formatDate.format(Date())

            user.createAt = createAt

            coroutineScope {
                val createJob = launch(Dispatchers.IO) {
                    println("createUser: I'm working in thread ${Thread.currentThread().name}")
                    createUser(id, user)
                }
                createJob.join()
                response.message = "Created success"
            }
        } catch (e: Exception) {
            response.error = true
            response.message = e.message.toString()
        }
        return response
    }

    suspend fun delete(id: String, user: UserDTO): Response {
        val response = Response(null)
        try {
            coroutineScope {
                val deleteJob = launch(Dispatchers.IO) {
                    println("deleteUser: I'm working in thread ${Thread.currentThread().name}")
                    deleteUser(id, user)
                }
                deleteJob.join()
                response.message = "Removed success"
            }
        } catch (e: EmptyResultDataAccessException) {
            response.error = true
            response.message = e.message.toString()
        }

        return response
    }

    suspend fun update(id: String, user: UserDTO): Response {
        val response = Response(null)
        val currentUserData = UserDTO()
        var currentUser = UserDynamoSchema()
        try {
            coroutineScope {
                val currentUserDeferred: Deferred<UserDynamoSchema> = async(Dispatchers.IO) {
                    println("FindByUuid: I'm working in thread ${Thread.currentThread().name}")
                    repository.findByUuid(id)
                }
                currentUser = currentUserDeferred.await()

                currentUserData.name = currentUser.getName()
                currentUserData.age = currentUser.age.toString()
                currentUserData.createAt = currentUser.createAt.toString()

                val deleteJob = launch(Dispatchers.IO) {
                    println("deleteUser: I'm working in thread ${Thread.currentThread().name}")
                    deleteUser(id, currentUserData)
                }
                deleteJob.join()

                user.createAt = currentUserData.createAt

                val createJob = launch(Dispatchers.IO) {
                    println("createUser: I'm working in thread ${Thread.currentThread().name}")
                    createUser(id, user)
                }
                createJob.join()
                response.message = "Update success"
            }
        } catch (e: Exception) {
            response.error = true
            response.message = e.message.toString()
        }
        return response
    }

    private fun deleteUser(id: String, user: UserDTO) {
        val userId = UserId()
        userId.uuid = id
        userId.name = user.name

        repository.deleteById(userId)
    }

    private fun createUser(id: String, user: UserDTO) {
        val userId = UserId()
        userId.uuid = id
        userId.name = user.name

        val schema = UserDynamoSchema()
        schema.id = userId
        schema.age = user.age

        if (user.createAt.isNotEmpty()) {
            schema.createAt = user.createAt
        }

        repository.save(schema)
    }
}