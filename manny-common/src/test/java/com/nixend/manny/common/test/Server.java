package com.nixend.manny.common.test;

/**
 * @author panyox
 */
public class Server {
    private String name;
    private Integer weight;
    private Integer currentWeight;

    public Server(String name, Integer weight, Integer currentWeight) {
        this.name = name;
        this.weight = weight;
        this.currentWeight = currentWeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(Integer currentWeight) {
        this.currentWeight = currentWeight;
    }
}
