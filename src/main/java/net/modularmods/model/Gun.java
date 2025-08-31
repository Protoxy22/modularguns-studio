package net.modularmods.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.modularmods.mvc.Model;
import java.util.HashMap;
import java.util.Map;

/**
 * Gun model representing a modular gun
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Gun extends Model {
    private String name;
    private String displayName;
    private float damage;
    private float range;
    private float fireRate;
    private boolean isAutomatic;
    private String modelPath;
    private String texturePath;
    private String soundPath;
    private Map<String, Object> customProperties = new HashMap<>();

    public Gun(String name) {
        this.name = name;
        this.displayName = name;
        this.damage = 50.0f;
        this.range = 100.0f;
        this.fireRate = 600.0f;
        this.isAutomatic = true;
    }

    public void setProperty(String key, Object value) {
        customProperties.put(key, value);
        notifyObservers(new ModelEvent(ModelEvent.Type.PROPERTY_CHANGED, this));
    }

    public Object getProperty(String key) {
        return customProperties.get(key);
    }
}
