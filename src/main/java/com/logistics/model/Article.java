package com.logistics.model;

public abstract class Article implements Stockable {
    private String id;
    private String name;
    private String reference;
    private double weight;
    private double length;
    private double width;
    private double height;

    public Article(String id, String name, String reference, double weight, double length, double width, double height) {
        this.id = id;
        this.name = name;
        this.reference = reference;
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return this.name;
    }

    public String getReference() {
        return this.reference;
    }

    public double getLength() {
        return this.length;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public int getFragilityLevel() {
        return 0;
    }

    public String displayInfo() {
        return name + " ("+reference+")";
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public double getVolume() {
        return this.length * this.width * this.height;
    }

    public abstract void prepareDelivery();
}
