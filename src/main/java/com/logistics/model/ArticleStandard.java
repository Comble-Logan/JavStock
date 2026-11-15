package com.logistics.model;

public class ArticleStandard extends Article {
    public ArticleStandard(String id, String name, String reference, double weight, double length, double width, double height) {
        super(id, name, reference, weight, length, width, height);
    }

    @Override
    public void prepareDelivery() {
        System.out.println("Preparing Article Standard and adding to the delivery palette");
    }
}
