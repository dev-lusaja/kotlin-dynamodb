package com.crud.dynamodb.schema

import com.amazonaws.services.dynamodbv2.datamodeling.*
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id

@DynamoDBTable(tableName = "kotlin")
class UserDynamoSchema(
    @Id
    @DynamoDBIgnore
    @JsonIgnore
    var id: UserId = UserId()
){
    @DynamoDBHashKey(attributeName = "uuid")
    fun getUuid() = id.uuid

    fun setUuid(uuid: String) {
        id.uuid = uuid
    }

    @DynamoDBRangeKey(attributeName = "name")
    fun getName() = id.name

    fun setName(name: String) {
        id.name = name
    }

    @DynamoDBAttribute(attributeName = "createAt")
    var createAt: String? = null

    @DynamoDBAttribute(attributeName = "age")
    var age: String? = null
}