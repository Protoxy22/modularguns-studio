package net.modularmods.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GunPack {
    private final String id;
    private String name;
    private String description;
    private String version;
    private final List<Gun> guns;
    private final List<Asset> assets;

    public GunPack(String name, String description, String version) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.version = version;
        this.guns = new ArrayList<>();
        this.assets = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public List<Gun> getGuns() { return new ArrayList<>(guns); }
    public List<Asset> getAssets() { return new ArrayList<>(assets); }

    public void addGun(Gun gun) {
        guns.add(gun);
    }

    public void removeGun(Gun gun) {
        guns.remove(gun);
    }

    public void addAsset(Asset asset) {
        assets.add(asset);
    }

    public void removeAsset(Asset asset) {
        assets.remove(asset);
    }

    @Override
    public String toString() {
        return name + " v" + version;
    }
}
