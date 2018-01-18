package com.example.elisabeth.depressionsapp.datamodel;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

/**
 * Created by doreen on 18.01.18.
 */

public class DynamoDbEntry {

    private final Map<String, AttributeValue> entry;

    public DynamoDbEntry(Map<String, AttributeValue> entry) {
        this.entry = entry;
    }

    public String getUserId() {
        return entry.get("userId").getS();
    }

    private Map<String,AttributeValue> getAttributes() {
        return entry.get("mapAttr").getM();
    }

    public int getScore() {
        String score = getAttributes().get("score").getN();
        return Integer.parseInt(score);
    }

    public int getTimesDone() {
        String timesDone = getAttributes().get("timesDone").getN();
        return Integer.parseInt(timesDone);
    }
}
