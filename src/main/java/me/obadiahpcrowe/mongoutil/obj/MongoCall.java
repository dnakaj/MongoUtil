package me.obadiahpcrowe.mongoutil.obj;

import com.google.gson.TypeAdapter;
import lombok.Getter;
import me.obadiahpcrowe.mongoutil.enums.CallType;

import java.util.Map;

/**
 * Created by: St1rling
 * Creation Date / Time: 11/8/17 at 11:57 AM
 * Project: MongoUtil
 * Package: me.obadiahpcrowe.mongoutil.obj
 * Copyright (c) Obadiah Crowe 2017
 */
@Getter
public class MongoCall {

    private String dbName;
    private MongoDB database;
    private CallType callType;
    private Map<String, Object> identifiers;
    private Map<Class, TypeAdapter> adapters;
    private Object insertableObject;
    private String updateField;
    private Class returnableObject;

    public MongoCall(String dbName, MongoDB database) {
        this.dbName = dbName;
        this.database = database;
    }

    public MongoCall setAdapters(Map<Class, TypeAdapter> adapters) {
        this.adapters = adapters;
        return this;
    }

    public MongoCall get(Map<String, Object> identifiers, Class returnableObject) {
        this.callType = CallType.GET;
        this.identifiers = identifiers;
        this.returnableObject = returnableObject;
        return this;
    }

    public MongoCall remove(Map<String, Object> identifiers) {
        this.callType = CallType.REMOVE;
        this.identifiers = identifiers;
        return this;
    }

    public MongoCall insert(Object insertableObject) {
        this.callType = CallType.INSERT;
        this.insertableObject = insertableObject;
        return this;
    }

    public MongoCall update(Map<String, Object> identifiers, String updateField, Object insertableObject) {
        this.callType = CallType.UPDATE;
        this.identifiers = identifiers;
        this.updateField = updateField;
        this.insertableObject = insertableObject;
        return this;
    }

    public MongoCall replace(Map<String, Object> identifiers, Object insertableObject) {
        this.callType = CallType.REPLACE;
        this.identifiers = identifiers;
        this.insertableObject = insertableObject;
        return this;
    }

    public MongoDB getDatabase() {
        return this.database;
    }
}
