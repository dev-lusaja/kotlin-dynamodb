package com.crud.dynamodb.service

data class Response(
    var data: Any?
    ) {
    var error: Boolean = false
    var message: String = ""
}