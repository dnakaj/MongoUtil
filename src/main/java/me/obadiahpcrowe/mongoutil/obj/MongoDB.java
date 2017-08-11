package me.obadiahpcrowe.mongoutil.obj;

import lombok.Getter;

/**
 * Created by: St1rling
 * Creation Date / Time: 11/8/17 at 11:52 AM
 * Project: MongoUtil
 * Package: me.obadiahpcrowe.mongoutil.obj
 * Copyright (c) Obadiah Crowe 2017
 */
@Getter
public class MongoDB {

    private String address;
    private int port;
    private String collection;
    private String username;
    private String password;

    public MongoDB(String address, int port, String collection) {
        this.address = address;
        this.port = port;
        this.collection = collection;
        this.username = "";
        this.password = "";
    }

    public MongoDB withCredentials(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }
}
