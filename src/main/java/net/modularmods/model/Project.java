package net.modularmods.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.modularmods.mvc.Model;
import java.util.ArrayList;
import java.util.List;

/**
 * Project model representing a modular guns project
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Project extends Model {
    private String name;
    private String version;
    private String description;
    private String author;
    private List<GunPack> gunPacks = new ArrayList<>();
    private List<Asset> assets = new ArrayList<>();
    private boolean modified = false;

    public Project(String name) {
        this.name = name;
        this.version = "1.0.0";
        this.description = "";
        this.author = System.getProperty("user.name");
    }

    public void addGunPack(GunPack gunPack) {
        gunPacks.add(gunPack);
        setModified(true);
        notifyObservers(new ModelEvent(ModelEvent.Type.ASSET_ADDED, gunPack));
    }

    public void removeGunPack(GunPack gunPack) {
        gunPacks.remove(gunPack);
        setModified(true);
        notifyObservers(new ModelEvent(ModelEvent.Type.ASSET_REMOVED, gunPack));
    }

    public void addAsset(Asset asset) {
        assets.add(asset);
        setModified(true);
        notifyObservers(new ModelEvent(ModelEvent.Type.ASSET_ADDED, asset));
    }

    public void removeAsset(Asset asset) {
        assets.remove(asset);
        setModified(true);
        notifyObservers(new ModelEvent(ModelEvent.Type.ASSET_REMOVED, asset));
    }

    public void setModified(boolean modified) {
        this.modified = modified;
        if (modified) {
            notifyObservers(new ModelEvent(ModelEvent.Type.PROPERTY_CHANGED, this));
        }
    }
}
