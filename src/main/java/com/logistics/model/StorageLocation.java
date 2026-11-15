package com.logistics.model;

public abstract class StorageLocation {
    private String id;
    private int level;
    private double weightMax;

    public StorageLocation(String id, int level, double weightMax) {
        this.id = id;
        this.level  = level;
        this.weightMax = weightMax;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public double getWeightMax() {
        return weightMax;
    }

    public abstract double getWeight();
    public abstract boolean isEmpty();
    public abstract String displayInfo();
}

