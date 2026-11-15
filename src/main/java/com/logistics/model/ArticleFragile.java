package com.logistics.model;

public class ArticleFragile extends Article {
    private int fragilityLevel;

    public ArticleFragile(String id, String name, String reference, double weight, double length, double width, double height, int fragilityLevel) {
        super(id, name, reference, weight, length, width, height);
    this.fragilityLevel = fragilityLevel;
    }

    @Override
    public int getFragilityLevel() {
        return this.fragilityLevel;
    }

    @Override
    public void prepareDelivery() {
        System.out.println("Preparing Article with protective packaging lvl " + this.fragilityLevel + " and adding to the delivery palette");
    }

    @Override
    public String displayInfo(){
        return super.displayInfo() + " - Fragile";
    }
}
