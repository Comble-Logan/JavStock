package com.logistics.service;

import com.logistics.exception.EmptyException;
import com.logistics.exception.RackNotFoundException;
import com.logistics.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LogisticsServiceTest {
    private LogisticsService logisticsService;
    private Map<String, Pallet> pallets;
    private Map<String, StorageLocationRack> racks;

    @BeforeEach
    void setUp() {
        Map<String, Article> articles = new HashMap<>();
        pallets = new HashMap<>();
        racks = new HashMap<>();

        articles.put("Art1", new ArticleStandard("Art1", "Article Test","REF-ART-111", 3.0, 7.3, 11.0, 9.1 ));
        pallets.put("Pal1", new Pallet("Pal1", 15.0, 100.0));
        pallets.put("Pal2", new Pallet("Pal2", 15.0, 100.0));

        StorageLocationRack storageLocationRack = new StorageLocationRack("Rck1");
        storageLocationRack.addStorageLocation(new StorageLocationPicking("R1-0", 0, 800.0));
        storageLocationRack.addStorageLocation(new StorageLocationReserve("R1-1", 1, 800.0));
        racks.put("Rck1", storageLocationRack);

        logisticsService = new LogisticsService(articles, pallets, racks);
    }


     @Test
    void testMovePallet() throws Exception {
        logisticsService.placePalletOnRack("Pal1", "Rck1", 1);
        logisticsService.movePallet("Rck1", 1, "Rck1", 0);

        StorageLocationPicking storageLocationPicking = (StorageLocationPicking) racks.get("Rck1").getLevels().get(0);
        assertEquals("Pal1", storageLocationPicking.getPallet().getId());
    }

    @Test
    void testMoveFromEmptyLocation() {
        assertThrows(EmptyException.class, () -> logisticsService.movePallet("Rck1", 1,  "Rck1", 0));
    }

    @Test
    void testDeletePackageArticle() throws Exception{
        logisticsService.createPackage("Pkg1", "Art1", 9, 12, 0.1, 35.0, 35.0, 35.0, "Pal1");
        logisticsService.deletePackage("Pkg1", "Pal1");
        assertTrue(pallets.get("Pal1").getPackageArticles().isEmpty());
    }


    @Test
    void testCreateRackAlreadyExist() {
        assertThrows(IllegalArgumentException.class, () -> logisticsService.createRack("Rck1"));
    }

    @Test
    void testDeleteUnknownId() {
        assertThrows(RackNotFoundException.class, () -> logisticsService.deleteRack("999"));
    }
}
