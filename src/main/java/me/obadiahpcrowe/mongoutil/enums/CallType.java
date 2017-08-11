package me.obadiahpcrowe.mongoutil.enums;

import lombok.Getter;

/**
 * Created by: St1rling
 * Creation Date / Time: 11/8/17 at 11:56 AM
 * Project: MongoUtil
 * Package: me.obadiahpcrowe.mongoutil.enums
 * Copyright (c) Obadiah Crowe 2017
 */
@Getter
public enum CallType {

    INSERT(false, 0),
    REMOVE(false, 1),
    GET(true, 2),
    REPLACE(false, 3),
    UPDATE(false, 4);

    private boolean returnsData;
    private int identifier;

    CallType(boolean returnsData, int identifier) {
        this.returnsData = returnsData;
        this.identifier = identifier;
    }
}
