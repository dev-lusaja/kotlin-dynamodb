package com.crud.dynamodb.service

data class UserInput(
    var name: String = "",
    var age: String = "",
    var createAt: String = ""
)