package DB;


import com.mongodb.ConnectionString;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.concurrent.CountDownLatch;


public class DBConnection {

    private static MongoDatabase db;
    private static DBConnection ourInstance = new DBConnection();
    String result;

    public static DBConnection getInstance() {
        return ourInstance;
    }

    private DBConnection() {
        MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://localhost:27017"));
        db = mongoClient.getDatabase("appivodb");
    }

    public void addUserData(String collection) {
        Object obj = JSONValue.parse(collection);
        JSONObject jsonObject = (JSONObject) obj;
        System.out.println(jsonObject.get("id"));
        System.out.println(jsonObject.get("foo"));
        MongoCollection<Document> userTable = db.getCollection("USER");
        Document userDoc = new Document();
        userDoc.append("id", jsonObject.get("id"));
        userDoc.append("foo", jsonObject.get("foo"));
        userTable.insertOne(userDoc, new SingleResultCallback<Void>() {
            public void onResult(final Void result, final Throwable t) {
                System.out.println("Inserted!");
            }
        });
    }

    public String getData(String getId) {
        try {
            final CountDownLatch latch = new CountDownLatch(1);
            Object obj = JSONValue.parse(getId);
            JSONObject jsonObject = (JSONObject) obj;
            MongoCollection<Document> userTable = db.getCollection("USER");
            Document query = new Document();
            query.append("id", jsonObject.get("id"));
            userTable.find(query).first(new SingleResultCallback<Document>() {

                public void onResult(final Document document, final Throwable t) {
                    result = document.toJson();
                    latch.countDown();
                }
            });
            latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }


}
