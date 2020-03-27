package com.example.demo;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;

import org.json.simple.JSONObject;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class DynamoDBService {

    private DynamoDB dynamoDb;
    private final String DYNAMODB_TABLE_NAME = "dynamo-table";
    private final Regions REGION = Regions.AP_NORTHEAST_1;

    public Message<String> operatingDynamoDB(Message<Person> inputMessage) {

        this.initDynamoDbClient();
        String responseMessage = "";

        try {
            Person person = (Person) inputMessage.getPayload();
            // System.out.println("payload person: " + person.toString());
            JSONObject responseBody = new JSONObject();
            
            if (person != null) {

                Table table = dynamoDb.getTable(DYNAMODB_TABLE_NAME);

                if (table != null) {
                    PutItemOutcome result = table.putItem(
                        new PutItemSpec().withItem(new Item()
                            .withNumber("id", person.getId())
                            .withString("name", person.getName())));
                    responseBody.put("message", person.toString());
                } else {
                    responseBody.put("message", "Requested resource not found");
                }
            } else {
                responseBody.put("message", "No data received");
            }
            responseMessage = new JSONObject(responseBody).toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return MessageBuilder.withPayload(responseMessage).build();
    }

    private void initDynamoDbClient() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(REGION));
        this.dynamoDb = new DynamoDB(client);
    }
}