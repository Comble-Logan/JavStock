package com.logistics.service;

import com.logistics.exception.*;
import com.logistics.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LogisticsService {
    private Map<String, Article> articles;
    private Map<String, Pallet> pallets;
    private Map<String, StorageLocationRack> racks;

    public LogisticsService(Map<String, Article> articles, Map<String, Pallet> pallets, Map<String, StorageLocationRack> racks) {
        this.articles = articles;
        this.pallets = pallets;
        this.racks = racks;
    }

    // GET

    public Map<String, Article> getArticles() {
        return articles;
    }

    public Map<String, Pallet> getPallets() {
        return pallets;
    }

    public Map<String, StorageLocationRack> getRacks() {
        return racks;
    }

    public StorageLocationPicking getStorageLocationPicking(String rackId, int level) throws RackNotFoundException, NotAPickingException {
        StorageLocationRack storageLocationRack = racks.get(rackId);
        if (storageLocationRack == null) {
            throw new RackNotFoundException("Rack " + rackId + " not found !");
        }
        StorageLocation storageLocation = storageLocationRack.getStorageLocation(level);
        if (!(storageLocation instanceof StorageLocationPicking)) {
            throw new NotAPickingException("The location isn't a picking !");
        }
        return (StorageLocationPicking) storageLocation;
    }


    public List<StorageLocationPicking> getAllPickingsLocations() {
        List<StorageLocationPicking> pickingList = new ArrayList<>();
        for (StorageLocationRack storageLocationRack : racks.values()) {
            for (StorageLocation storageLocation : storageLocationRack.getLevels().values()) {
                if (storageLocation instanceof StorageLocationPicking) {
                    StorageLocationPicking storageLocationPicking = (StorageLocationPicking) storageLocation;
                    if (!storageLocationPicking.isEmpty()) {
                        pickingList.add(storageLocationPicking);
                    }
                }
            }
        }
        return pickingList;
    }


    public List<StorageLocationReserve> getAllReserveLocations() {
        List<StorageLocationReserve> reserveList = new ArrayList<>();
        for (StorageLocationRack storageLocationRack : racks.values()) {
            for (StorageLocation storageLocation : storageLocationRack.getLevels().values()) {
                if (storageLocation instanceof StorageLocationReserve) {
                    StorageLocationReserve storageLocationReserve = (StorageLocationReserve) storageLocation;
                    if (!storageLocationReserve.isEmpty()) {
                        reserveList.add(storageLocationReserve);
                    }
                }
            }
        }
        return reserveList;
    }


    public List<Pallet> getAllPallets() {
        List<Pallet> palletList = new ArrayList<>();
        for (StorageLocationPicking storageLocationPicking : getAllPickingsLocations()) {
            palletList.add(storageLocationPicking.getPallet());
        }
        for (StorageLocationReserve storageLocationReserve : getAllReserveLocations()) {
            palletList.add(storageLocationReserve.getPallet());
        }
        return palletList;
    }


    public Optional<StorageLocationReserve> getNewPalletForPicking(String articleReference) {
        for (StorageLocationReserve storageLocationReserve : getAllReserveLocations()) {
            List<PackageArticle> packageArticles = storageLocationReserve.getPallet().getPackageArticles();

            if (!packageArticles.isEmpty() && packageArticles.get(0).getArticle().getReference().equals(articleReference)) {
                return Optional.of(storageLocationReserve);
            }
        }
        return Optional.empty();
    }

    // CREATE

    public void createArticle(Article article) {
        if (articles.containsKey(article.getId())) {
            throw new IllegalArgumentException("Article ID already exists !");
        }
        articles.put(article.getId(), article);
    }

    public void createPallet(Pallet pallet) {
        if (pallets.containsKey(pallet.getId())) {
            throw new IllegalArgumentException("Pallet ID already exists !");
        }
        pallets.put(pallet.getId(), pallet);
    }

    public void createRack(String rackId) {
        if (racks.containsKey(rackId)) {
            throw new IllegalArgumentException("Rack ID already exists !");
        }
        racks.put(rackId, new StorageLocationRack(rackId));
    }

    public void createLocationForRack(String rackId, StorageLocation storageLocation) throws RackNotFoundException {
        StorageLocationRack rack = racks.get(rackId);
        if (rack == null) {
            throw new RackNotFoundException("Rack " + rackId + " not found !");
        }
        rack.addStorageLocation(storageLocation);
    }

    public void createPackage(String packageId, String articleId, int quantity, int quantityMax, double emptyWeight, double length, double width, double height, String palletId) throws CapacityExceededException, WeightExceededException, PalletNotFoundException, ArticleNotFoundException{
        Pallet pallet = pallets.get(palletId);
        if (pallet == null) {
            throw new PalletNotFoundException("Pallet " + palletId + " not found !");
        }
        Article article = articles.get(articleId);
        if (article == null) {
            throw new ArticleNotFoundException("Article " + articleId + " not found !");
        }

        PackageArticle newPackageArticle = new PackageArticle(packageId, article, quantityMax, emptyWeight, length, width, height);
        newPackageArticle.addArticle(quantity);
        pallet.addPackageArticle(newPackageArticle);
    }

    // DELETE

    public void deleteArticle(String articleId) throws ArticleNotFoundException {
        if (articles.remove(articleId) == null) {
            throw new ArticleNotFoundException("Article " + articleId + " not found !");
        }
    }

    public void deletePallet(String palletId) throws PalletNotFoundException {
        if (!pallets.containsKey(palletId)) {
            throw new PalletNotFoundException("Pallet " + palletId + " not found !");
        }
        boolean cantBeDelete = getAllPallets().stream().anyMatch(pallet -> pallet.getId().equals(palletId));
        if (cantBeDelete) {
            throw new IllegalArgumentException("Pallet " + palletId + " is on a rack and cannot be deleted !");
        }
        pallets.remove(palletId);
    }

    public void deleteRack(String rackId) throws RackNotFoundException {
        StorageLocationRack storageLocationRack = racks.get(rackId);
        if (storageLocationRack == null) {
            throw new RackNotFoundException("Rack " + rackId + " not found !");
        }
        racks.remove(rackId);
    }

    public void deletePackage(String packageId, String palletId) throws PackageNotFoundException, PalletNotFoundException {
        Pallet pallet = pallets.get(palletId);
        if (pallet == null) {
            throw new PalletNotFoundException("Pallet " + palletId + " not found !");
        }
        PackageArticle removePackage = pallet.getPackageArticles().stream()
                .filter(pkg -> pkg.getId().equals(packageId))
                .findFirst()
                .orElseThrow(() -> new PackageNotFoundException("Package " + packageId + " not found "));
        pallet.removePackageArticle(removePackage);
    }

    // MOVE

    public void movePallet(String originRackId, int originLevel, String newRackId, int newLevel) throws RackNotFoundException, EmptyException, WeightExceededException, CapacityExceededException {
        StorageLocationRack originRack = racks.get(originRackId);
        StorageLocationRack newRack = racks.get(newRackId);
        if (originRack == null) {
            throw new RackNotFoundException("Rack " + originRackId + " not found !");
        }
        if (newRack == null) {
            throw new RackNotFoundException("Rack " + newRackId + " not found !");
        }

        StorageLocation source = originRack.getStorageLocation(originLevel);
        StorageLocation target = newRack.getStorageLocation(newLevel);
        if (source == null) {
            throw new RackNotFoundException("Level " + originLevel + " in rack " + originRackId + " doesn't exists !");
        }
        if (target == null) {
            throw new RackNotFoundException("Level " + newLevel + " in rack " + newRackId + " doesn't exists !");
        }

        Pallet movingPallet;
        if (source instanceof StorageLocationPicking) {
            StorageLocationPicking storageLocationPicking = (StorageLocationPicking) source;
            if (storageLocationPicking.isEmpty()) {
                throw new  EmptyException("The source position is empty !");
            }
            movingPallet = storageLocationPicking.movePallet();
        } else if (source instanceof StorageLocationReserve) {
            StorageLocationReserve storageLocationReserve = (StorageLocationReserve) source;
            if (storageLocationReserve.isEmpty()) {
                throw new  EmptyException("The source position is empty !");
            }
            movingPallet = storageLocationReserve.movePallet();
        } else {
            throw new IllegalArgumentException("Location type is invalid !");
        }

        try {
            if (target instanceof StorageLocationPicking) {
                StorageLocationPicking storageLocationPicking = (StorageLocationPicking) target;
                storageLocationPicking.addPallet(movingPallet);
            } else if (target instanceof StorageLocationReserve) {
                StorageLocationReserve storageLocationReserve = (StorageLocationReserve) target;
                storageLocationReserve.addPallet(movingPallet);
            }
        } catch (WeightExceededException | CapacityExceededException e) {
            if (source instanceof StorageLocationPicking) {
                StorageLocationPicking storageLocationPicking = (StorageLocationPicking) source;
                storageLocationPicking.addPallet(movingPallet);
            } else if (source instanceof StorageLocationReserve) {
                StorageLocationReserve storageLocationReserve = (StorageLocationReserve) source;
                storageLocationReserve.addPallet(movingPallet);
            }
            throw e;
        }
    }

    public void placePalletOnRack(String palletId, String rackId, int level) throws PalletNotFoundException, RackNotFoundException, WeightExceededException, CapacityExceededException {
        Pallet pallet = pallets.get(palletId);
        if (pallet == null) {
            throw new PalletNotFoundException("Pallet " + palletId + " not found !");
        }

        boolean alreadyPlaced = getAllPallets().stream().anyMatch(p -> p.getId().equals(palletId));
        if (alreadyPlaced) {
            throw new IllegalArgumentException("Pallet " + palletId + " is already placed !");
        }

        StorageLocationRack storageLocationRack = racks.get(rackId);
        if (storageLocationRack == null) {
            throw new RackNotFoundException("Rack " + rackId + " not found !");
        }
        StorageLocation storageLocation = storageLocationRack.getStorageLocation(level);
        if (storageLocation instanceof StorageLocationPicking) {
            StorageLocationPicking storageLocationPicking = (StorageLocationPicking) storageLocation;
            storageLocationPicking.addPallet(pallet);
        } else if (storageLocation instanceof StorageLocationReserve) {
            StorageLocationReserve storageLocationReserve = (StorageLocationReserve) storageLocation;
            storageLocationReserve.addPallet(pallet);
        } else {
            throw new RackNotFoundException("Level " + level + " not found in rack " + rackId + " !");
        }
    }

    public Optional<String> takePackage(String rackId, int level) throws PackageNotFoundException, RackNotFoundException, NotAPickingException, EmptyException, IllegalArgumentException {

        StorageLocationRack storageLocationRack = racks.get(rackId);

        if (storageLocationRack == null) {
            throw new RackNotFoundException("Rack " + rackId + " not found !");
        }

        StorageLocation storageLocation = storageLocationRack.getStorageLocation(level);
        if (!(storageLocation instanceof StorageLocationPicking)) {
            throw new NotAPickingException("Picking storage location is not picking !");
        }

        StorageLocationPicking storageLocationPicking = (StorageLocationPicking) storageLocation;
        if (storageLocationPicking.isEmpty()) {
            throw new EmptyException("Empty storage location !");
        }

        Pallet pallet = storageLocationPicking.getPallet();
        if (pallet.getPackageArticles().isEmpty()) {
            throw new EmptyException("Empty package articles !");
        }
        PackageArticle tackingPackage = pallet.getPackageArticles().get(0);
        Article article = tackingPackage.getArticle();
        article.prepareDelivery();
        String reference = article.getReference();
        storageLocationPicking.takePackageFromPicking(tackingPackage);

        if (storageLocationPicking.isEmpty()) {
            return Optional.of(reference);
        }
        return Optional.empty();
    }

    public void replacePicking(StorageLocationReserve storageLocationReserve, StorageLocationPicking storageLocationPicking) throws EmptyException, WeightExceededException, CapacityExceededException {
        if (storageLocationReserve.isEmpty()) {
            throw new EmptyException("Location " + storageLocationReserve.getId() + " is empty !");
        }

        Pallet pallet = storageLocationReserve.movePallet();
        try {
            storageLocationPicking.addPallet(pallet);
        } catch (CapacityExceededException | WeightExceededException e) {
            storageLocationReserve.addPallet(pallet);
            throw e;
        }
    }
}
