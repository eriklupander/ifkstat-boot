package se.ifkgoteborg.stat.controller;

import se.ifkgoteborg.stat.model.Ground;

/**
 * Created by eriklupander on 2015-07-25.
 */
public class GroundDataDto {
    private final Ground key;
    private final Integer size;

    public GroundDataDto(Ground key, Integer size) {
        this.key = key;
        this.size = size;
    }

    public Ground getKey() {
        return key;
    }

    public Integer getSize() {
        return size;
    }
}
