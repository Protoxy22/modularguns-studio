package net.modularmods.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class Asset {
    public enum Type {
        MODEL("3D Model", new String[]{".obj", ".fbx", ".gltf", ".glb"}),
        TEXTURE("Texture", new String[]{".png", ".jpg", ".jpeg", ".tga", ".dds"}),
        ANIMATION("Animation", new String[]{".anim", ".fbx"}),
        SOUND("Sound", new String[]{".wav", ".ogg", ".mp3"}),
        ICON("Icon", new String[]{".png", ".jpg", ".jpeg"}),
        DATA("Data", new String[]{".json", ".xml", ".yaml"});

        private final String displayName;
        private final String[] extensions;

        Type(String displayName, String[] extensions) {
            this.displayName = displayName;
            this.extensions = extensions;
        }

        public String getDisplayName() { return displayName; }
        public String[] getExtensions() { return extensions; }
    }

    private final String id;
    private String name;
    private final Type type;
    private final String filePath;
    private final String fileName;
    private final String fileExtension;
    private final long fileSize;
    private boolean isLoaded;

    public Asset(String filePath, Type type) {
        this.id = UUID.randomUUID().toString();
        this.filePath = filePath;
        this.type = type;

        Path path = Paths.get(filePath);
        this.fileName = path.getFileName().toString();
        this.name = getNameWithoutExtension(fileName);
        this.fileExtension = getFileExtension(fileName);
        this.fileSize = 0; // TODO: Get actual file size
        this.isLoaded = false;
    }

    private String getNameWithoutExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(0, lastDotIndex) : fileName;
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex) : "";
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Type getType() { return type; }
    public String getFilePath() { return filePath; }
    public String getFileName() { return fileName; }
    public String getFileExtension() { return fileExtension; }
    public long getFileSize() { return fileSize; }
    public boolean isLoaded() { return isLoaded; }
    public void setLoaded(boolean loaded) { isLoaded = loaded; }

    @Override
    public String toString() {
        return name + " (" + type.getDisplayName() + ")";
    }
}
