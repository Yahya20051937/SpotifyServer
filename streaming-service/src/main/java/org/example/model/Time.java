package org.example.model;

import lombok.Data;

@Data
public class Time {
    private int hour;
    private int minutes;
    private int seconds;

    public Time(int value) {
        this.hour = value / 3600;
        this.minutes = (value % 3600) / 60;
        this.seconds = (value % 3600) % 60;
    }

    public int getSecondsValue() {
        return this.seconds + this.minutes * 60 + this.hour * 3600;
    }
}