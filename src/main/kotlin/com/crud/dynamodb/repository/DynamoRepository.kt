package com.crud.dynamodb.repository
import com.crud.dynamodb.schema.UserDynamoSchema
import com.crud.dynamodb.schema.UserId
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

@EnableScan
interface DynamoRepository : CrudRepository<UserDynamoSchema, UserId> {
    fun findByUuid(uuid: String): UserDynamoSchema
    fun findByName(name: String): List<UserDynamoSchema>
}