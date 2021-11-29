package com.crud.dynamodb.service

import com.crud.dynamodb.schema.DynamoSchema
import com.crud.dynamodb.repository.DynamoRepository
import com.crud.dynamodb.schema.UserId
import kotlinx.coroutines.*
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*

@Service
class DynamoService(val repository: DynamoRepository) {
    fun findByUuid(uuid: String): Response = runBlocking {
        val response = Response(null)
        try {
            coroutineScope {
                val data: Deferred<DynamoSchema> = async(Dispatchers.IO) { repository.findByUuid(uuid) }
                response.data = data.await()
            }
        } catch (e: Exception) {
            response.error = true
            response.message = e.message.toString()
        }
        return@runBlocking response
    }

    fun findByName(name: String): Response {
        val response = Response(null)
        try {
            response.data = repository.findByName(name)
        } catch (e: EmptyResultDataAccessException) {
            response.error = true
            response.message = e.message.toString()
        }
        return response
    }

    fun findAll(): Response {
        val response = Response(null)
        try {
            response.data = repository.findAll()
        } catch (e: Exception) {
            response.error = true
            response.message = e.message.toString()
        }
        return response
    }

    fun save(user: UserInput): Response {
        val response = Response(null)
        try {
            val id: String = UUID.randomUUID().toString()
            val formatDate = SimpleDateFormat("dd/MM/yyyy")
            val createAt = formatDate.format(Date())

            val userId = UserId()
            userId.uuid = id
            userId.name = user.name

            val schema = DynamoSchema()
            schema.id = userId
            schema.createAt = createAt
            schema.age = user.age

            repository.save(schema)
            response.message = "Created success"
        } catch (e: Exception) {
            response.error = true
            response.message = e.message.toString()
        }
        return response
    }

    fun delete(id: String, user: UserInput): Response {
        val response = Response(null)
        try {
            val userId = UserId()
            userId.uuid = id
            userId.name = user.name
            repository.deleteById(userId)
            response.message = "Removed success"
        } catch (e: EmptyResultDataAccessException) {
            response.error = true
            response.message = e.message.toString()
        }

        return response
    }

    fun update(id: String, user: UserInput): Response {
        val response = Response(null)
        try {
            val currentUser = repository.findByUuid(id)
            val currentUserData = UserInput()
            currentUserData.name = currentUser.getName()
            currentUserData.age = currentUser.age.toString()
            currentUserData.createAt = currentUser.createAt.toString()

            delete(id, currentUserData)

            val userId = UserId()
            userId.uuid = id
            userId.name = user.name

            val schema = DynamoSchema()
            schema.id = userId
            schema.createAt = user.createAt
            schema.age = user.age

            repository.save(schema)
            response.message = "Update success"
        } catch (e: Exception) {
            response.error = true
            response.message = e.message.toString()
        }
        return response
    }
}