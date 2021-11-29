package com.crud.dynamodb.repository

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableDynamoDBRepositories("com.crud.dynamodb.repository")
class DynamoDBConfig {

    @Value("\${config.env}")
    var env: String? = null

    @Value("\${config.region}")
    var region: String? = null

    // Only needed to load localhost endpoint for development
    // With established AWS configuration, endpoint isn't needed
    @Value("\${aws.dynamodb.endpoint:}")
    var amazonDynamoDBEndpoint: String? = null

    @Bean
    fun amazonDynamoDB(): AmazonDynamoDB {
        val builder = AmazonDynamoDBClientBuilder.standard()

        println ( "DB aws.dynamodb.endpoint = $amazonDynamoDBEndpoint" )

        if (amazonDynamoDBEndpoint?.isNotBlank() ?: false) {
            println ( "Using local Dynamo DB." )
            builder.withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, region))
        }

        return builder.build()
    }
}