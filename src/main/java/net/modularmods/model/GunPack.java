package net.modularmods.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.modularmods.mvc.Model;
import java.util.ArrayList;
import java.util.List;

/**
 * Gun pack model representing a collection of guns
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GunPack extends Model {
    private String name;
    private String description;
    private List<Gun> guns = new ArrayList<>();

    public GunPack(String name) {
        this.name = name;
        this.description = "";
    }

    public void addGun(Gun gun) {
        guns.add(gun);
        notifyObservers(new ModelEvent(ModelEvent.Type.ASSET_ADDED, gun));
    }

    public void removeGun(Gun gun) {
        guns.remove(gun);
        notifyObservers(new ModelEvent(ModelEvent.Type.ASSET_REMOVED, gun));
    }
}
