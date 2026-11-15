package com.logistics.model;
import com.logistics.exception.PackageNotFoundException;
import com.logistics.exception.WeightExceededException;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Pallet implements Stockable {
    private String id;
    private double emptyWeight;
    private double length;
    private double width;
    private double height;
    private double weightMax;
    private List<PackageArticle> content;

    public Pallet(String id, double emptyWeight, double weightMax) {
        this.id = id;
        this.emptyWeight = emptyWeight;
        this.weightMax = weightMax;
        this.length = 120.0;
        this.width = 80.0;
        this.height = 0.0;
        this.content = new ArrayList<>();
    }

    public void addPackageArticle(PackageArticle packageArticle) throws WeightExceededException {
        if (!this.content.isEmpty()) {
            String articleReference = this.getPackageArticles().get(0).getArticle().getId();
            if (!articleReference.equals(packageArticle.getArticle().getId())) {
                throw new IllegalArgumentException("A pallet cannot have different articles !");
            }
        }

        if (this.getWeight() + packageArticle.getWeight() > this.weightMax) {
            throw new WeightExceededException("Package weight exceeds maximum weight !");
        }
        this.content.add(packageArticle);
    }

    public void removePackageArticle(PackageArticle packageArticle) throws PackageNotFoundException {
        boolean isRemoved = this.content.remove(packageArticle);
        if (!isRemoved) {
            throw new PackageNotFoundException("Package not found !");
        }
    }

    public List<PackageArticle> getPackageArticles() {
        return new ArrayList<>(this.content);
    }

    public String displayInfo(){
        if (getPackageArticles().isEmpty()){
            return "Palette " + this.id + " (" + getWeight() + " kg) --- [ Empty ]";
        }
        String content = getPackageArticles().stream()
                .map(PackageArticle::displayInfo)
                .collect(Collectors.joining(", "));
        return "Palette " + this.id + " (" + getWeight() + " kg) --- [ " + content + " ]";
    }

    public double getEmptyWeight() {
        return emptyWeight;
    }

    public double getWeightMax() {
        return weightMax;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public double getWeight() {
        double contentWeight = this.content.stream()
                .mapToDouble(PackageArticle::getWeight)
                .sum();
        return this.emptyWeight + contentWeight;
    }

    @Override
    public double getVolume(){
        return this.length * this.width * this.height;
    }
}
