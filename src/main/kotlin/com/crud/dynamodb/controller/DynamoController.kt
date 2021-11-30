package com.crud.dynamodb.controller

import com.crud.dynamodb.service.DynamoService
import com.crud.dynamodb.service.Response
import com.crud.dynamodb.dto.UserDTO
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.*

@RestController
class DynamoController(val service: DynamoService) {

    @GetMapping("/users")
    @ResponseBody
    fun findAll(): Response = runBlocking {
        println("Controller: I'm working in thread ${Thread.currentThread().name}")
        val deferred = async { service.findAll() }
        return@runBlocking deferred.await()
    }

    @GetMapping("/users/{uuid}")
    @ResponseBody
    fun findByUuid(@PathVariable uuid: String): Response = runBlocking {
        println("Controller: I'm working in thread ${Thread.currentThread().name}")
        val deferred = async { service.findByUuid(uuid) }
        return@runBlocking deferred.await()
    }

    @GetMapping("/users/search/{name}")
    @ResponseBody
    fun findByName(@PathVariable name: String): Response = runBlocking {
        println("Controller: I'm working in thread ${Thread.currentThread().name}")
        val deferred = async { service.findByName(name) }
        return@runBlocking deferred.await()
    }

    @PostMapping("/users")
    fun save(@RequestBody user: UserDTO): Response = runBlocking {
        println("Controller: I'm working in thread ${Thread.currentThread().name}")
        val deferred = async { service.save(user) }
        return@runBlocking deferred.await()
    }

    @DeleteMapping("/users/{id}")
    fun delete(@PathVariable id: String, @RequestBody user: UserDTO): Response = runBlocking {
        println("Controller: I'm working in thread ${Thread.currentThread().name}")
        val deferred = async { service.delete(id, user) }
        return@runBlocking deferred.await()
    }

    @PutMapping("/users/{id}")
    fun update(@PathVariable id: String, @RequestBody user: UserDTO): Response = runBlocking {
        println("Controller: I'm working in thread ${Thread.currentThread().name}")
        val deferred = async { service.update(id, user) }
        return@runBlocking deferred.await()
    }
}