package com.logistics.service;
import com.logistics.exception.CapacityExceededException;
import com.logistics.exception.SavingCsvException;
import com.logistics.exception.WeightExceededException;
import com.logistics.model.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class CsvService {

    // IMPORT


    // Import Articles
    public Map<String, Article> loadCsvArticles(String filePath) {
        Map<String, Article> articles = new HashMap<>();

        try (BufferedReader buffered = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = buffered.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] splitedData = line.split(";");
                if (splitedData.length < 9) {
                    continue;
                }

                String id = splitedData[0];
                String name = splitedData[1];
                String reference = splitedData[2];
                double weight = Double.parseDouble(splitedData[3]);
                double length = Double.parseDouble(splitedData[4]);
                double width = Double.parseDouble(splitedData[5]);
                double height = Double.parseDouble(splitedData[6]);
                String type  = splitedData[7];
                int fragilityLevel =  Integer.parseInt(splitedData[8]);

                Article article;
                if (type.equals("Fragile")) {
                    article = new ArticleFragile(id, name, reference, weight, length, width, height, fragilityLevel);
                } else if (type.equals("Standard")) {
                    article = new ArticleStandard(id, name, reference, weight, length, width, height);
                } else {
                    throw new IllegalArgumentException("Unknown article type: " + type);
                }

                articles.put(id, article);
            }

            System.out.println(articles.size() + " articles loaded !");
        } catch (IOException e) {
            System.out.println("Error while reading file : " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error with CSV format : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error with CSV format : " + e.getMessage());
        }

        return articles;
    }

    // Import pallets
    public Map<String, Pallet> loadCsvPallets(String filePath) {
        Map<String, Pallet> pallets = new HashMap<>();

        try (BufferedReader buffered = new BufferedReader(new FileReader(filePath))){
            String line;
            boolean firstLine = true;

            while ((line = buffered.readLine()) != null){
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] splitedData = line.split(";");
                if (splitedData.length < 3) {
                    continue;
                }
                String id = splitedData[0];
                double emptyWeight = Double.parseDouble(splitedData[1]);
                double weightMax = Double.parseDouble(splitedData[2]);

                pallets.put(id, new Pallet(id, emptyWeight, weightMax));
            }
            System.out.println(pallets.size() + " pallets loaded !");
        } catch (IOException e) {
            System.out.println("Error while reading file : " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error with CSV format : " + e.getMessage());
        }

        return pallets;
    }

    // Import packages
    public void loadCsvPackages(String filePath, Map<String, Article> articles, Map<String, Pallet> pallets ) {
        int counter = 0;

        try (BufferedReader buffered = new BufferedReader(new FileReader(filePath))){
            String line;
            boolean firstLine = true;

            while ((line = buffered.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] splitedData = line.split(";");
                if (splitedData.length < 9) {
                    continue;
                }
                String id = splitedData[0];
                String articleId = splitedData[1];
                int quantity = Integer.parseInt(splitedData[2]);
                int quantityMax = Integer.parseInt(splitedData[3]);
                double emptyWeight = Double.parseDouble(splitedData[4]);
                double length = Double.parseDouble(splitedData[5]);
                double width = Double.parseDouble(splitedData[6]);
                double height = Double.parseDouble(splitedData[7]);
                String palletId = splitedData[8];

                Article article = articles.get(articleId);
                Pallet pallet = pallets.get(palletId);

                if (article == null) {
                    System.out.println("Unknown article ID: " + articleId);
                    continue;
                } else if (pallet == null) {
                    System.out.println("Unknown pallet ID: " + palletId);
                    continue;
                }

                PackageArticle packageArticle = new PackageArticle(id, article, quantityMax, emptyWeight, length, width, height);
                packageArticle.addArticle(quantity);
                pallet.addPackageArticle(packageArticle);
                counter++;
            }
            System.out.println(counter + " packages loaded !");
        } catch (IOException e) {
            System.out.println("Error while reading file : " + e.getMessage());
        } catch (NumberFormatException e)  {
            System.out.println("Error with CSV format : " + e.getMessage());
        } catch (CapacityExceededException e)  {
            System.out.println("Capacity exceeded : " + e.getMessage());
        } catch (WeightExceededException e) {
            System.out.println("Weight exceeded : " + e.getMessage());
        }
    }

    // Import Locations
    public Map<String, StorageLocationRack> loadCsvLocations(String filePath, Map<String, Pallet> pallets) {
        Map<String, StorageLocationRack> racks = new HashMap<>();

        try (BufferedReader buffered = new BufferedReader(new FileReader(filePath))){
            String line;
            boolean firstLine = true;

            while ((line = buffered.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] splitedData = line.split(";");
                if (splitedData.length < 6) {
                    continue;
                }
                String rackId = splitedData[0];
                String locationId = splitedData[1];
                String type = splitedData[2];
                int level =  Integer.parseInt(splitedData[3]);
                double weightMax =  Double.parseDouble(splitedData[4]);
                String palletId = splitedData[5];


                if (!racks.containsKey(rackId)) {
                    racks.put(rackId, new StorageLocationRack(rackId));
                }
                StorageLocationRack rack = racks.get(rackId);

                StorageLocation location;

                if (type.equals("Picking")) {
                    StorageLocationPicking picking = new StorageLocationPicking(locationId, level, weightMax);
                    if (!palletId.equals("None") && pallets.containsKey(palletId)) {
                        picking.addPallet(pallets.get(palletId));
                    }
                    location = picking;
                } else if (type.equals("Reserve")) {
                    StorageLocationReserve reserve = new StorageLocationReserve(locationId, level, weightMax);
                    if (!palletId.equals("None") && pallets.containsKey(palletId)) {
                        reserve.addPallet(pallets.get(palletId));
                    }
                    location = reserve;
                } else {
                    throw new IllegalArgumentException("Unknown location type: " + type);
                }
                rack.addStorageLocation(location);
            }
            System.out.println(racks.size() + " racks loaded !");
        } catch (IOException e) {
            System.out.println("Error while reading file : " + e.getMessage());
        } catch (NumberFormatException e)  {
            System.out.println("Error with CSV format : " + e.getMessage());
        } catch (CapacityExceededException e)  {
            System.out.println("Capacity exceeded : " + e.getMessage());
        } catch (WeightExceededException e) {
            System.out.println("Weight exceeded : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error with CSV format : " + e.getMessage());
        }

        return racks;
    }



    // SAVE

    public void saveCsvArticles(String filePath, Map<String, Article> articles) throws SavingCsvException{
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))){
            bufferedWriter.write("id;name;reference;weight;length;width;height;type;fragilityLevel");
            bufferedWriter.newLine();

            for (Article article : articles.values()) {
                String articleType;
                int articleFragility;

                if (article instanceof ArticleFragile) {
                    articleType = "Fragile";
                    articleFragility = ((ArticleFragile) article).getFragilityLevel();
                } else if (article instanceof ArticleStandard){
                    articleType = "Standard";
                    articleFragility = 0;
                } else {
                    throw new IllegalArgumentException("Unknown article type: " + article.getClass().getName());
                }

                bufferedWriter.write(article.getId() + ";" + article.getName() + ";" + article.getReference() + ";" + article.getWeight() + ";" + article.getLength() + ";" + article.getWidth() + ";" + article.getHeight() + ";" + articleType + ";" +  articleFragility );
                bufferedWriter.newLine();
            }
        } catch (IOException e){
            throw new SavingCsvException("An error occurred while trying to save articles : " + e.getMessage());
        }
    }

    public void saveCsvPackages(String filePath, Map<String, Pallet> pallets) throws SavingCsvException{
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))){
            bufferedWriter.write("id;articleId;quantity;quantityMax;emptyWeight;length;width;height;palletId");
            bufferedWriter.newLine();

            for (Pallet pallet : pallets.values()) {
                for (PackageArticle packageArticle : pallet.getPackageArticles()) {
                    bufferedWriter.write(packageArticle.getId() + ";" + packageArticle.getArticle().getId() + ";" + packageArticle.getQuantity() + ";" + packageArticle.getQuantityMax() + ";" + packageArticle.getEmptyWeight() + ";" + packageArticle.getLength() + ";" + packageArticle.getWidth() + ";" + packageArticle.getHeight() + ";" + pallet.getId());
                    bufferedWriter.newLine();
                }
            }

        } catch (IOException e){
            throw new SavingCsvException("An error occurred while trying to save packages : " + e.getMessage());
        }
    }

    public void saveCsvPallets(String filePath, Map<String, Pallet> pallets) throws SavingCsvException{
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))){
            bufferedWriter.write("id;emptyWeight;weightMax");
            bufferedWriter.newLine();

            for (Pallet pallet : pallets.values()) {
                bufferedWriter.write(pallet.getId() + ";" + pallet.getEmptyWeight() + ";" + pallet.getWeightMax());
                bufferedWriter.newLine();
            }
        } catch (IOException e){
            throw new SavingCsvException("An error occurred while trying to save pallets : " + e.getMessage());
        }
    }

    public void saveCsvLocations(String filePath, Map<String, StorageLocationRack> racks) throws SavingCsvException{
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))){
            bufferedWriter.write("rackId;locationId;type;level;weightMax;palletId");
            bufferedWriter.newLine();

            for (StorageLocationRack storageLocationRack : racks.values()) {
                for (StorageLocation storageLocation : storageLocationRack.getLevels().values()) {
                    String type;
                    String palletId;

                    if (storageLocation instanceof StorageLocationPicking) {
                        type = "Picking";
                        StorageLocationPicking storageLocationPicking = (StorageLocationPicking) storageLocation;
                        if (storageLocationPicking.isEmpty()) {
                            palletId = "None";
                        } else {
                            palletId = storageLocationPicking.getPallet().getId();
                        }
                    } else {
                        type = "Reserve";
                        StorageLocationReserve storageLocationReserve = (StorageLocationReserve) storageLocation;
                        if (storageLocationReserve.isEmpty()) {
                            palletId = "None";
                        }  else {
                            palletId = storageLocationReserve.getPallet().getId();
                        }
                    }
                    bufferedWriter.write(storageLocationRack.getId() + ";" + storageLocation.getId() + ";" + type + ";" + storageLocation.getLevel() + ";" + storageLocation.getWeightMax() + ";" + palletId);
                    bufferedWriter.newLine();
                }
            }
        } catch (IOException e){
            throw new SavingCsvException("An error occurred while trying to save racks : " + e.getMessage());
        }
    }


    public void saveAll(String articlesFilePath, Map<String, Article> articles, String packageFilePath, String palletsFilePath, Map<String, Pallet> pallets, String locationsFilePath, Map<String, StorageLocationRack> racks) throws SavingCsvException{
        saveCsvArticles(articlesFilePath, articles);
        saveCsvPallets(palletsFilePath, pallets);
        saveCsvPackages(packageFilePath, pallets);
        saveCsvLocations(locationsFilePath, racks);
    }

}

