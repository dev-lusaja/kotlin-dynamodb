package com.crud.dynamodb.repository
import com.crud.dynamodb.schema.DynamoSchema
import com.crud.dynamodb.schema.UserId
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

@EnableScan
interface DynamoRepository : CrudRepository<DynamoSchema, UserId> {
    fun findByUuid(uuid: String?): DynamoSchema
    fun findByName(name: String): List<DynamoSchema>
}