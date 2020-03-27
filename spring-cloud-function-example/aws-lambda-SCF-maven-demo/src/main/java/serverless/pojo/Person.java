package serverless.pojo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Person {
 
    private String id;
    private String name;
    private String email;
 
    public Person(String json) {
        Gson gson = new Gson();
        Person request = gson.fromJson(json, Person.class);
        this.id = request.getId();
        this.name = request.getName();
        this.email = request.getEmail();
    }

    public Person(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
 
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}