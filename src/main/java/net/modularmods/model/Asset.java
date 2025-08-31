package net.modularmods.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.modularmods.mvc.Model;
import java.io.File;
import java.time.LocalDateTime;

/**
 * Asset model representing various types of assets (textures, models, sounds, etc.)
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Asset extends Model {
    public enum Type {
        TEXTURE,
        MODEL,
        SOUND,
        ANIMATION,
        SCRIPT,
        CONFIG
    }

    private String name;
    private Type type;
    private String filePath;
    private long fileSize;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String description;

    public Asset(String name, Type type, String filePath) {
        this.name = name;
        this.type = type;
        this.filePath = filePath;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();

        // Calculate file size if file exists
        File file = new File(filePath);
        if (file.exists()) {
            this.fileSize = file.length();
        }
    }

    public void updateModifiedTime() {
        this.modifiedAt = LocalDateTime.now();
        notifyObservers(new ModelEvent(ModelEvent.Type.PROPERTY_CHANGED, this));
    }
}
