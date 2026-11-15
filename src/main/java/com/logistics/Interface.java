package com.logistics;

import com.logistics.exception.SavingCsvException;
import com.logistics.model.*;
import com.logistics.service.CsvService;
import com.logistics.service.LogisticsService;

import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Interface {
    private static final Scanner scanner = new Scanner(System.in);
    private static CsvService csvService = new CsvService();
    private static LogisticsService logisticsService;
    private static String articleCsvPath = "src/main/resources/articles.csv";
    private static String packageCsvPath = "src/main/resources/packages.csv";
    private static String palletCsvPath = "src/main/resources/pallets.csv";
    private static String locationsCsvPath = "src/main/resources/locations.csv";


    public static void main(String[] args) {
        System.out.println("----- Welcome to JavStock ----- \n");
        System.out.println("Data initialization...");

        Map<String, Article> articles = csvService.loadCsvArticles(articleCsvPath);
        Map<String, Pallet> pallets = csvService.loadCsvPallets(palletCsvPath);
        csvService.loadCsvPackages(packageCsvPath, articles, pallets);
        Map<String, StorageLocationRack> racks = csvService.loadCsvLocations(locationsCsvPath, pallets);
        logisticsService = new LogisticsService(articles, pallets, racks);

        System.out.println("Initialization complete !");
        System.out.println("Starting interface... \n");

        boolean exit = false;
        while (!exit) {
            displayMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        displayInventory();
                        break;
                    case "2":
                        handleCreationMenu();
                        updateCsvData();
                        break;
                    case "3":
                        handleMovePalletMenu();
                        updateCsvData();
                        break;
                    case "4":
                        handleOrderMenu();
                        updateCsvData();
                        break;
                    case "5":
                        handleDeleteMenu();
                        updateCsvData();
                        break;
                    case "6":
                        exit = true;
                        System.out.println("Exiting ...");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                        break;
                }
            } catch (NumberFormatException e){
                System.out.println("Invalid choice. Try again.");
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n--------------- MENU --------------- \n");
        System.out.println("1. Display warehouse inventory");
        System.out.println("2. Display creation menu");
        System.out.println("3. Display movement menu");
        System.out.println("4. Order gestion menu");
        System.out.println("5. Delete menu");
        System.out.println("6. Exit \n");
        System.out.println("Your choice : ");
    }


    private static void displayInventory() {
        System.out.println("\n--------------- INVENTORY --------------- \n");
        Map<String, StorageLocationRack> racks = logisticsService.getRacks();

        if (racks.isEmpty()) {
            System.out.println("There is no Rack available !");
            return;
        }
        for (StorageLocationRack rack : racks.values()) {
            System.out.println(rack.displayInfo());
        }
    }

    private static void handleCreationMenu() throws Exception{
        System.out.println("\n--------------- CREATION MENU --------------- \n");
        System.out.println("1. Create new article");
        System.out.println("2. Create new pallet");
        System.out.println("3. Create new package");
        System.out.println("4. Create new Rack");
        System.out.println("5. Create new level");
        System.out.println("6. Back to main menu");
        System.out.println("Your choice : ");
        String createChoice = scanner.nextLine().trim();
        switch (createChoice) {
            case "1":
                System.out.println("Article ID : ");
                String articleId = scanner.nextLine().trim();
                System.out.println("Name Article : ");
                String articleName = scanner.nextLine().trim();
                System.out.println("Reference Article : ");
                String articleReference = scanner.nextLine().trim();
                System.out.println("Weight Article (kg) : ");
                double  articleWeight = Double.parseDouble(scanner.nextLine().trim());
                System.out.println("Length Article (cm): ");
                double  articleLength = Double.parseDouble(scanner.nextLine().trim());
                System.out.println("Width Article (cm): ");
                double  articleWidth = Double.parseDouble(scanner.nextLine().trim());
                System.out.println("Height Article (cm): ");
                double  articleHeight = Double.parseDouble(scanner.nextLine().trim());
                System.out.println("Type (Standard / Fragile) :");
                String articleType = scanner.nextLine().trim();

                if (articleType.equals("Standard")) {
                    logisticsService.createArticle(new ArticleStandard(articleId, articleName, articleReference, articleWeight, articleLength, articleWidth, articleHeight));
                } else if (articleType.equals("Fragile")) {
                    System.out.println("Fragility level (1 to 5) : ");
                    int fragileLevel = Integer.parseInt(scanner.nextLine().trim());
                    logisticsService.createArticle(new ArticleFragile(articleId, articleName, articleReference, articleWeight, articleLength, articleWidth, articleHeight, fragileLevel));
                } else {
                    System.out.println("Incorrect type of article");
                    break;
                }
                System.out.println("Article " + articleId + " has been successfully created !");
                break;

            case "2":
                System.out.println("Pallet ID : ");
                String palletId = scanner.nextLine().trim();
                System.out.println("Pallet weight when empty (kg) : ");
                double palletEmptyWeight = Double.parseDouble(scanner.nextLine().trim());
                System.out.println("Max pallet weight (kg) : ");
                double palletMaxWeight = Double.parseDouble(scanner.nextLine().trim());

                logisticsService.createPallet(new Pallet(palletId,palletEmptyWeight,palletMaxWeight));
                System.out.println("Pallet " + palletId + " has been successfully created !");
                break;

            case "3":
                System.out.println("Package ID : ");
                String packageId = scanner.nextLine().trim();
                System.out.println("Article ID : ");
                String packageArticle =  scanner.nextLine().trim();
                System.out.println("Article max quantity : ");
                int packageMaxQuantity = Integer.parseInt(scanner.nextLine().trim());
                System.out.println("Article quantity : ");
                int packageQuantity = Integer.parseInt(scanner.nextLine().trim());
                System.out.println("Package weight when empty (kg) : ");
                double packageEmptyWeight = Double.parseDouble(scanner.nextLine().trim());
                System.out.println("Length Package (cm) : ");
                double packageLength = Double.parseDouble(scanner.nextLine().trim());
                System.out.println("Package width : ");
                double packageWidth = Double.parseDouble(scanner.nextLine().trim());
                System.out.println("Package height : ");
                double packageHeight = Double.parseDouble(scanner.nextLine().trim());
                System.out.println("Pallet ID :");
                String packagePallet = scanner.nextLine().trim();

                logisticsService.createPackage(packageId, packageArticle, packageQuantity, packageMaxQuantity, packageEmptyWeight, packageLength, packageWidth, packageHeight, packagePallet);
                System.out.println("Package " + packageId + " has been successfully created !");
                break;

            case "4":
                System.out.println("ID rack : ");
                String rackId = scanner.nextLine().trim();
                logisticsService.createRack(rackId);
                System.out.println("Rack " + rackId + " has been successfully created !");
                break;

            case "5":
                System.out.println("Rack target ID : ");
                String rackTargetId = scanner.nextLine().trim();
                System.out.println("Level ID : ");
                String levelId = scanner.nextLine().trim();
                System.out.println("Level (0 : Pickhing / 0 and more for Reserve : ");
                int levelNumber = Integer.parseInt(scanner.nextLine().trim());
                System.out.println("Max Weight (kg) : ");
                double levelMaxWeight = Double.parseDouble(scanner.nextLine().trim());
                System.out.println("Type (Picking / Reserve) : ");
                String levelType = scanner.nextLine().trim();

                StorageLocation levelStorageLocation;
                if (levelType.equals("Picking")) {
                    levelStorageLocation = new StorageLocationPicking(levelId, levelNumber, levelMaxWeight);
                } else if (levelType.equals("Reserve")) {
                    levelStorageLocation = new StorageLocationReserve(levelId, levelNumber, levelMaxWeight);
                } else {
                    System.out.println("Incorrect type of level");
                    break;
                }
                logisticsService.createLocationForRack(rackTargetId, levelStorageLocation);
                System.out.println("Level " + levelId + " on " + rackTargetId + " has been successfully created !");
                break;

            case "6":
                break;

            default:
                System.out.println("Invalid choice. Try again.");
                break;
        }

    }

    private static void handleMovePalletMenu() throws Exception {
        System.out.println("\n--------------- MOVEMENT MENU --------------- \n");
        System.out.println("1. Assign a location for a new pallet");
        System.out.println("2. Move a pallet between two locations");
        System.out.println("3. Back to main menu");
        System.out.println("Your choice :");
        String moveChoice =  scanner.nextLine().trim();
        switch (moveChoice) {
            case "1":
                System.out.println("Pallet ID : ");
                String palletId = scanner.nextLine().trim();
                System.out.println("Rack ID : ");
                String rackId = scanner.nextLine().trim();
                System.out.println("Level : ");
                int level = Integer.parseInt(scanner.nextLine().trim());

                logisticsService.placePalletOnRack(palletId, rackId, level);
                System.out.println("Pallet " + palletId + " has been successfully placed !");
                break;

            case  "2":
                System.out.println("Origin rack ID : ");
                String originRackId = scanner.nextLine().trim();
                System.out.println("Origin level : ");
                int originLevel = Integer.parseInt(scanner.nextLine().trim());
                System.out.println("Target rack ID : ");
                String targetRackId = scanner.nextLine().trim();
                System.out.println("Target level : ");
                int targetLevel = Integer.parseInt(scanner.nextLine().trim());

                logisticsService.movePallet(originRackId, originLevel, targetRackId, targetLevel);
                System.out.println("Pallet has been successfully moved !");
                break;
            case "3":
                break;
            default:
                System.out.println("Invalid choice. Try again.");
                break;
        }
    }

    private static void handleOrderMenu() throws Exception {
        System.out.println("\n--------------- ORDER MENU --------------- \n");
        System.out.println("Command reference : ");
        String commandId = scanner.nextLine().trim();
        System.out.println("Rack ID : ");
        String rackId = scanner.nextLine().trim();

        Optional<String> missingReference = logisticsService.takePackage(rackId, 0);
        System.out.println("Package has been successfully taken for command " + commandId + " !");

        if (!missingReference.isPresent()) {
            return;
        }

        replacementPicking(rackId, missingReference.get());
    }

    private static void replacementPicking(String rackId, String packageReference) throws Exception {
        System.out.println("Picking is empty, trying to find a new pallet in reserve..");
        Optional<StorageLocationReserve> reserve = logisticsService.getNewPalletForPicking(packageReference);

        if (!reserve.isPresent()) {
            System.out.println("No replacement pallet available !");
            return;
        }

        StorageLocationReserve storageLocationReserve = reserve.get();
        System.out.println("Replacement found (" + storageLocationReserve.getPallet().getId() + " in " + storageLocationReserve.getId() + ").");
        System.out.println("Try to move into the picking ? (Y/N) : ");
        if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
            StorageLocationPicking storageLocationPicking = logisticsService.getStorageLocationPicking(rackId, 0);
            logisticsService.replacePicking(storageLocationReserve, storageLocationPicking);
            System.out.println("Picking has been successfully replaced !");
        }
    }


    private static void handleDeleteMenu() throws Exception {
        System.out.println("\n--------------- DELETE MENU --------------- \n");
        System.out.println("1. Delete an article");
        System.out.println("2. Delete a package from a pallet");
        System.out.println("3. Delete a pallet");
        System.out.println("4. Delete a rack");
        System.out.println("5. Back to main menu");
        System.out.println("Your choice : ");
        String deleteChoice = scanner.nextLine().trim();
        switch (deleteChoice) {
            case "1":
                System.out.println("Article ID : ");
                String articleId = scanner.nextLine().trim();
                logisticsService.deleteArticle(articleId);
                System.out.println("Article " + articleId + " has been successfully deleted !");
                break;
            case "2":
                System.out.println("Package ID : ");
                String packageId = scanner.nextLine().trim();
                System.out.println("Pallet ID : ");
                String palletId = scanner.nextLine().trim();
                logisticsService.deletePackage(packageId, palletId);
                System.out.println("Package " + packageId + " has been successfully deleted !");
                break;
            case "3":
                System.out.println("Pallet ID : ");
                String palletID = scanner.nextLine().trim();
                logisticsService.deletePallet(palletID);
                System.out.println("Pallet " + palletID + " has been successfully deleted !");
                break;
            case "4":
                System.out.println("ID rack : ");
                String rackId = scanner.nextLine().trim();
                logisticsService.deleteRack(rackId);
                System.out.println("Rack " + rackId + " has been successfully deleted !");
                break;

            case "5":
                break;

            default:
                System.out.println("Invalid choice. Try again.");
        }

    }

    private static void updateCsvData() throws SavingCsvException {
        csvService.saveAll(articleCsvPath, logisticsService.getArticles(), packageCsvPath, palletCsvPath, logisticsService.getPallets(), locationsCsvPath, logisticsService.getRacks());
    }
}
