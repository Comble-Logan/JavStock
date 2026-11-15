package com.logistics.model;

import com.logistics.exception.CapacityExceededException;
import com.logistics.exception.WeightExceededException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class StorageLocationTest {

    @Test
    void testExceedsLocationMaxWeight() {
        StorageLocationReserve storageLocationReserve = new StorageLocationReserve("01", 1, 100);
        Pallet pallet = new Pallet("01", 300.0, 300);
        assertThrows(WeightExceededException.class, () -> storageLocationReserve.addPallet(pallet));
    }

    @Test
    void testOccupiedLocation() throws CapacityExceededException, WeightExceededException {
        StorageLocationReserve storageLocationReserve = new StorageLocationReserve("01", 1, 100);
        Pallet pallet1 = new Pallet("01", 15.0, 10);
        Pallet pallet2 = new Pallet("02", 15.0, 10);
        storageLocationReserve.addPallet(pallet1);
        assertThrows(CapacityExceededException.class, () -> storageLocationReserve.addPallet(pallet2));
    }

}
