package com.crud.dynamodb.schema

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey
import java.io.Serializable

class UserId : Serializable {
    @field:DynamoDBHashKey
    var uuid: String = ""

    @field:DynamoDBRangeKey
    var name: String = ""
}