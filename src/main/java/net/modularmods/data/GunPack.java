package net.modularmods.data;

import java.util.ArrayList;
import java.util.List;

public class GunPack {
    private PackMetadata metadata;
    private List<String> models;
    private List<String> textures;
    private List<String> animations;
    private List<String> stats;
    private List<String> icons;

    public GunPack(PackMetadata metadata) {
        this.metadata = metadata;
        this.models = new ArrayList<>();
        this.textures = new ArrayList<>();
        this.animations = new ArrayList<>();
        this.stats = new ArrayList<>();
        this.icons = new ArrayList<>();
    }

    // Getters
    public PackMetadata getMetadata() { return metadata; }
    public List<String> getModels() { return models; }
    public List<String> getTextures() { return textures; }
    public List<String> getAnimations() { return animations; }
    public List<String> getStats() { return stats; }
    public List<String> getIcons() { return icons; }

    // Utility methods
    public int getTotalAssets() {
        return models.size() + textures.size() + animations.size() + stats.size() + icons.size();
    }

    public boolean isEmpty() {
        return getTotalAssets() == 0;
    }
}
