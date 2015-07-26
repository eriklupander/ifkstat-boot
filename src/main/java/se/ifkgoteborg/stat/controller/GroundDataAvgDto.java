package se.ifkgoteborg.stat.controller;

import se.ifkgoteborg.stat.model.Ground;

/**
 * Created by eriklupander on 2015-07-25.
 */
public class GroundDataAvgDto {
    private final Ground key;
    private final Double average;

    public GroundDataAvgDto(Ground key, Double average) {
        this.key = key;
        this.average = average;
    }

    public Ground getKey() {
        return key;
    }

    public Double getAverage() {
        return average;
    }
}
