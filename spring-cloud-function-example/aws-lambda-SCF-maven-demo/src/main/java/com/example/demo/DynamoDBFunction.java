package com.example.demo;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("dynamoDBFunction")
public class DynamoDBFunction implements Function<Message<Person>, Message<String>> {

    private final DynamoDBService dynamoDBService;

    @Autowired
    public DynamoDBFunction(final DynamoDBService dynamoDBService) {
        this.dynamoDBService = dynamoDBService;
    }

    @Override
    public Message<String> apply(Message<Person> inputMessage) {
        // System.out.println("inputMessage: " + inputMessage.getPayload().toString());
        return dynamoDBService.operatingDynamoDB(inputMessage);
    }
    
}