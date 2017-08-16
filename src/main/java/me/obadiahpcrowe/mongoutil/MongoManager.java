package me.obadiahpcrowe.mongoutil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import me.obadiahpcrowe.mongoutil.obj.MongoCall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by: St1rling
 * Creation Date / Time: 11/8/17 at 11:58 AM
 * Project: MongoUtil
 * Package: me.obadiahpcrowe.mongoutil
 * Copyright (c) Obadiah Crowe 2017
 */
public class MongoManager {

    private static MongoManager instance;
    private Gson gson = new Gson();

    public Object makeCall(MongoCall mongoCall) {
        String username = mongoCall.getDatabase().getUsername();
        String password = mongoCall.getDatabase().getPassword();

        MongoClient mongoClient = null;

        if (!Objects.equals(username, "") && !Objects.equals(password, "")) {
            MongoCredential credential = MongoCredential.createCredential(username, mongoCall.getDbName(), password.toCharArray());
            mongoClient = new MongoClient(new ServerAddress(mongoCall.getDatabase().getAddress(), mongoCall.getDatabase().getPort()),
              Arrays.asList(credential));
        } else {
            mongoClient = new MongoClient(mongoCall.getDatabase().getAddress(), mongoCall.getDatabase().getPort());
        }

        DB db = mongoClient.getDB(mongoCall.getDbName());
        DBCollection collection = db.getCollection(mongoCall.getDatabase().getCollection());
        Object returnableObject = null;

        switch (mongoCall.getCallType()) {
            case GET:
                returnableObject = getObject(collection, mongoCall.getIdentifiers(), mongoCall.getReturnableObject(), mongoCall.getAdapters());
                break;
            case INSERT:
                insertObject(collection, mongoCall.getInsertableObject(), mongoCall.getAdapters());
                break;
            case REMOVE:
                deleteObject(collection, mongoCall.getIdentifiers());
                break;
            case UPDATE:
                updateObject(collection, mongoCall.getIdentifiers(), mongoCall.getUpdateField(), mongoCall.getInsertableObject());
                break;
        }

        mongoClient.close();
        return returnableObject;
    }

    private Object getObject(DBCollection collection, Map<String, Object> identifiers, Class returnableObject) { return getObject(collection, identifiers, returnableObject, null); }
    private Object getObject(DBCollection collection, Map<String, Object> identifiers, Class returnableObject, Map<Class, TypeAdapter> adapters) {
        BasicDBObject query = new BasicDBObject();
        Gson gson = this.gson;
        if (adapters != null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            for (Map.Entry<Class, TypeAdapter> entrySet : adapters.entrySet()) {
                gsonBuilder.registerTypeAdapter(entrySet.getKey(), entrySet.getValue());
            }
            gson = gsonBuilder.create();
        }

        if (identifiers.size() <= 0) {
            DBCursor cursor = collection.find();
            List<Object> objects = new ArrayList<>();
            while (cursor.hasNext()) {
                String json = gson.toJson(cursor.next());
                objects.add(gson.fromJson(json, returnableObject));
            }

            return gson.toJson(objects.toArray());
        } else {
            Iterator itr = identifiers.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry next = (Map.Entry) itr.next();

                query.append(next.getKey().toString(), next.getValue());
            }
        }

        DBCursor cursor = collection.find(query);
        if (cursor.hasNext()) {
            String json = gson.toJson(cursor.next());
            return gson.fromJson(json, returnableObject);
        } else {
            return null;
        }
    }
    private void insertObject(DBCollection collection, Object insertableObject) { insertObject(collection, insertableObject, null); }
    private void insertObject(DBCollection collection, Object insertableObject, Map<Class, TypeAdapter> adapters) {
        Gson gson = this.gson;
        if (adapters != null) {;
            GsonBuilder gsonBuilder = new GsonBuilder();
            for (Map.Entry<Class, TypeAdapter> entrySet : adapters.entrySet()) {
                gsonBuilder.registerTypeAdapter(entrySet.getKey(), entrySet.getValue());
            }
            gson = gsonBuilder.create();
        }

        BasicDBObject query = BasicDBObject.parse(gson.toJson(insertableObject));
        collection.insert(query);
    }

    private void deleteObject(DBCollection collection, Map<String, Object> identifiers) {
        BasicDBObject query = new BasicDBObject();
        Iterator itr = identifiers.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry next = (Map.Entry) itr.next();
            query.append(next.getKey().toString(), next.getValue());
        }

        collection.remove(query);
    }

    private void updateObject(DBCollection collection, Map<String, Object> identifiers, String updateField,
                              Object insertableObject) {
        BasicDBObject object = new BasicDBObject();
        object.append("$set", new BasicDBObject().append(updateField, insertableObject));
        BasicDBObject query = new BasicDBObject();
        Iterator itr = identifiers.entrySet().iterator();

        while (itr.hasNext()) {
            Map.Entry next = (Map.Entry) itr.next();
            query.append(next.getKey().toString(), next.getValue());
        }

        collection.update(query, object);
    }

    public static MongoManager getInstance() {
        if (instance == null)
            instance = new MongoManager();
        return instance;
    }
}
