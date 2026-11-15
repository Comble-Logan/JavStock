package com.logistics.model;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StorageLocationRack  {
    private String id;
    private Map<Integer, StorageLocation> levels;

    public StorageLocationRack(String id) {
        this.id = id;
        this.levels = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public Map<Integer, StorageLocation> getLevels() {
        return levels;
    }

    public void addStorageLocation(StorageLocation storageLocation) {
        this.levels.put(storageLocation.getLevel(), storageLocation);
    }

    public StorageLocation getStorageLocation(int level) {
        return this.levels.get(level);
    }

    public String displayInfo() {
        if(getLevels().isEmpty()) {
            return "Rack " + this.id + " --- [ Empty ]";
        }
        String content = getLevels().values().stream()
                .map(location -> " - " + location.displayInfo())
                .collect(Collectors.joining("\n"));

        return "Rack " + this.id + " :\n" + content;
    }
}
