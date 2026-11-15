package com.logistics.model;

import com.logistics.exception.CapacityExceededException;
import com.logistics.exception.WeightExceededException;

public class StorageLocationReserve extends StorageLocation {
    private Pallet pallet;

    public StorageLocationReserve(String id, int level, double weightMax) {
        super(id, level, weightMax);
        this.pallet = null;
    }

    public void addPallet(Pallet pallet) throws CapacityExceededException, WeightExceededException {
        if (this.pallet != null) {
            throw new CapacityExceededException("Pallet already exists at this location !");
        }

        if (pallet.getWeight() > this.getWeightMax()) {
            throw new WeightExceededException("Weight exceeded for this location !");
        }

        this.pallet = pallet;
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
        } else
            return this.pallet.getWeight();
    }
    @Override
    public boolean isEmpty() {
        return this.pallet == null;
    }

    @Override
    public String displayInfo() {
        return "Lvl : " + getLevel() + " [Reserve] : " + (isEmpty() ? "Empty" : pallet.displayInfo());
    }
}
