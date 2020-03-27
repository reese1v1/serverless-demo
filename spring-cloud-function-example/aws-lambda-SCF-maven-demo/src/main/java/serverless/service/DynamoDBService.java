package serverless.service;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;

import org.json.simple.JSONObject;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import serverless.pojo.Person;

@Service
public class DynamoDBService {

    private final String DYNAMODB_TABLE_NAME = "STV-Lamdba-Demo-Person";
    private final Regions REGION = Regions.AP_NORTHEAST_1;
    private DynamoDB dynamoDb;

    public Message<String> operatingDynamoDB(Message<Person> inputMessage) {
        // init
        this.initDynamoDbClient();
        
        JSONObject responseJson = new JSONObject();
        try {
            JSONObject responseBody = new JSONObject();

            if (inputMessage != null && inputMessage.getPayload() != null) {
                // Generate document
                final Person person = (Person) inputMessage.getPayload();
                // database access
                final Table table = dynamoDb.getTable(DYNAMODB_TABLE_NAME);
                if (table != null) {
                    table.putItem(
                        new PutItemSpec().withItem(new Item()
                            .withNumber("id", Integer.valueOf(person.getId()))
                            .withString("name", person.getName())
                            .withString("email", person.getEmail())));
                    responseBody.put("message", person.toString());
                } else {
                    responseBody.put("message", "Requested resource not found");
                }
            } else {
                responseBody.put("message", "No data received");
            }
            responseJson.put("statusCode", 200);
            responseJson.put("body", responseBody.toString());

        } catch (Exception e) {
            responseJson.put("statusCode", 400);
            responseJson.put("exception", e);
        }

        return MessageBuilder.withPayload(responseJson.toString()).build();
    }

    private void initDynamoDbClient() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(REGION));
        this.dynamoDb = new DynamoDB(client);
    }
}