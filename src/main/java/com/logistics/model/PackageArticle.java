package com.logistics.model;

import com.logistics.exception.CapacityExceededException;
import com.logistics.exception.EmptyException;


public class PackageArticle implements Stockable {
    private String id;
    private Article article;
    private int quantity;
    private int quantityMax;
    private double emptyWeight;
    private double length;
    private double width;
    private double height;


    public PackageArticle(String id, Article article, int quantityMax, double emptyWeight, double length, double width, double height) {
        this.id = id;
        this.article = article;
        this.quantity = 0;
        this.quantityMax = quantityMax;
        this.emptyWeight = emptyWeight;
        this.length = length;
        this.width = width;
        this.height = height;
    }

    public void addArticle(int quantityAdded) throws CapacityExceededException {
        if (this.quantity + quantityAdded > quantityMax) {
            throw new CapacityExceededException("This package is already full !");
        }
        this.quantity += quantityAdded;
    }

    public void removeArticle(int quantityRemoved) throws EmptyException {
        if (this.quantity - quantityRemoved < 0) {
            throw new EmptyException("This package is empty !");
        }
        this.quantity -= quantityRemoved;
    }

    public Article getArticle() {
        return article;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getQuantityMax() {
        return quantityMax;
    }

    public double getEmptyWeight() {
        return emptyWeight;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }

    public String displayInfo() {
        return "[" + id + " : " + quantity + " x " + article.displayInfo() + "]";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public double getWeight() {
        return (this.article.getWeight() * this.quantity) + this.emptyWeight;
    }

    @Override
    public double getVolume() {
        return this.length * this.width * this.height;
    }

    public int getFragilityLevel() {
        return this.article.getFragilityLevel();
    }
}
