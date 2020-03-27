package serverless.function;

import java.util.function.Function;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import org.springframework.stereotype.Component;

import serverless.pojo.Person;

@Component("cosmosDBCreateSCF")
public class CosmosDBCreateFunction implements Function<String, String> {

    @Override
    public String apply(String body) throws JsonParseException {

        final JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
        // Generate document
        Person person = new Person(jsonObject.toString());
    
        // Generate document
        return person.toString();
    }
    
}