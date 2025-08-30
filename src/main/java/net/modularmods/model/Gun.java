package net.modularmods.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Gun {
    private final String id;
    private String name;
    private String description;
    private final Map<String, Object> stats;
    private Asset modelAsset;
    private Asset textureAsset;
    private Asset animationAsset;
    private Asset iconAsset;

    public Gun(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = "";
        this.stats = new HashMap<>();
        initializeDefaultStats();
    }

    private void initializeDefaultStats() {
        stats.put("damage", 30.0f);
        stats.put("range", 100.0f);
        stats.put("fireRate", 600.0f);
        stats.put("accuracy", 0.8f);
        stats.put("recoil", 0.3f);
        stats.put("weight", 3.5f);
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Map<String, Object> getStats() { return new HashMap<>(stats); }
    public void setStat(String key, Object value) { stats.put(key, value); }
    public Object getStat(String key) { return stats.get(key); }

    public Asset getModelAsset() { return modelAsset; }
    public void setModelAsset(Asset modelAsset) { this.modelAsset = modelAsset; }
    public Asset getTextureAsset() { return textureAsset; }
    public void setTextureAsset(Asset textureAsset) { this.textureAsset = textureAsset; }
    public Asset getAnimationAsset() { return animationAsset; }
    public void setAnimationAsset(Asset animationAsset) { this.animationAsset = animationAsset; }
    public Asset getIconAsset() { return iconAsset; }
    public void setIconAsset(Asset iconAsset) { this.iconAsset = iconAsset; }

    @Override
    public String toString() {
        return name;
    }
}
