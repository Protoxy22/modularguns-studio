package net.modularmods.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Project {
    private final String id;
    private String name;
    private String path;
    private String description;
    private final List<GunPack> gunPacks;

    public Project(String name, String path) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.path = path;
        this.description = "";
        this.gunPacks = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<GunPack> getGunPacks() { return new ArrayList<>(gunPacks); }

    public void addGunPack(GunPack pack) {
        gunPacks.add(pack);
    }

    public void removeGunPack(GunPack pack) {
        gunPacks.remove(pack);
    }

    @Override
    public String toString() {
        return name;
    }
}
