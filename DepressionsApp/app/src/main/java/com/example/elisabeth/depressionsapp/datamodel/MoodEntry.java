package com.example.elisabeth.depressionsapp.datamodel;

/**
 * Created by elisabeth on 06.01.18.
 */
public class MoodEntry {
    public int id;
    public int score;
    public String timestamp;

    public MoodEntry() {}

    public MoodEntry(int score, String timestamp) {
        this.score = score;
        this.timestamp = timestamp;
    }

    public MoodEntry(int id, int score, String timestamp) {
        this.id = id;
        this.score = score;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
