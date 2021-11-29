package com.crud.dynamodb.controller

import com.crud.dynamodb.service.DynamoService
import com.crud.dynamodb.service.Response
import com.crud.dynamodb.service.UserInput
import org.springframework.web.bind.annotation.*

@RestController
class DynamoController(val service: DynamoService) {

    @GetMapping("/users")
    @ResponseBody
    fun findAll(): Response {
        return service.findAll()
    }

    @GetMapping("/users/{uuid}")
    @ResponseBody
    fun findByUuid(@PathVariable uuid: String): Response {
        return service.findByUuid(uuid)
    }

    @GetMapping("/users/search/{name}")
    @ResponseBody
    fun findByName(@PathVariable name: String): Response {
        return service.findByName(name)
    }

    @PostMapping("/users")
    fun save(@RequestBody user: UserInput): Response {
        return service.save(user)
    }

    @DeleteMapping("/users/{id}")
    fun delete(@PathVariable id: String, @RequestBody user: UserInput): Response {
        return service.delete(id, user)
    }

    @PutMapping("/users/{id}")
    fun update(@PathVariable id: String, @RequestBody user: UserInput): Response {
        return service.update(id, user)
    }
}