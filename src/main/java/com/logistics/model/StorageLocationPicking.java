package com.logistics.model;

import com.logistics.exception.CapacityExceededException;
import com.logistics.exception.EmptyException;
import com.logistics.exception.PackageNotFoundException;
import com.logistics.exception.WeightExceededException;

public class StorageLocationPicking extends StorageLocation {
    private Pallet pallet;

    public StorageLocationPicking(String id, int level, double weightMax) {
        super(id, level, weightMax);

        if (level != 0) {
            throw new IllegalArgumentException("Level must be 0");
        }

        this.pallet = null;
    }

    public void addPallet(Pallet pallet) throws WeightExceededException, CapacityExceededException {
        if (this.pallet != null) {
            throw new CapacityExceededException("Pallet already exists at this location !");
        }

        if (pallet.getWeight() > this.getWeightMax()) {
            throw new WeightExceededException("Pallet weight exceeds maximum weight !");
        }

        this.pallet = pallet;
    }

    public void takePackageFromPicking(PackageArticle packageTaken) throws PackageNotFoundException, EmptyException {
        if (this.pallet == null) {
            throw new EmptyException("There is no pallet at this location !");
        }
        this.pallet.removePackageArticle(packageTaken);

        if (this.pallet.getPackageArticles().isEmpty()) {
            this.pallet = null;
        }
    }

    public Pallet getPallet() {
        return pallet;
    }

    public Pallet movePallet() {
        Pallet pallet = this.pallet;
        this.pallet = null;
        return pallet;
    }

    @Override
    public double getWeight() {
        if (this.pallet == null) {
            return 0.0;
        } else {
            return this.pallet.getWeight();
        }
    }


    @Override
    public boolean isEmpty() {
        return this.pallet == null;
    }

    @Override
    public String displayInfo() {
        return "Lvl : " + getLevel() + " [Picking] : " + (isEmpty() ? "Empty" : pallet.displayInfo());
    }
}
