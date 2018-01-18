package com.example.elisabeth.depressionsapp.database;

/**
 * Created by doreen on 17.01.18.
 */

import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.example.elisabeth.depressionsapp.datamodel.DynamoDbEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamoDB {

    private AmazonDynamoDBClient dynamoDBClient;

    public DynamoDB() {
        this.dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
    }

    public List<DynamoDbEntry> getEntries() {
        ScanRequest request = new ScanRequest().withTableName("WHOScoreTableMAPH");
        ScanResult result = dynamoDBClient.scan(request);

        ArrayList<DynamoDbEntry> parsedEntries = new ArrayList<>();
        for (Map<String, AttributeValue> item : result.getItems()) {
            parsedEntries.add(new DynamoDbEntry(item));
        }

        return parsedEntries;
    }
}
