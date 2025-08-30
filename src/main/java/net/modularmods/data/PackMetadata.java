package net.modularmods.data;

public class PackMetadata {
    private String name;
    private String version;
    private String description;
    private long createdTime;
    private long modifiedTime;

    public PackMetadata(String name, String version, String description) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.createdTime = System.currentTimeMillis();
        this.modifiedTime = System.currentTimeMillis();
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
        updateModifiedTime();
    }

    public String getVersion() { return version; }
    public void setVersion(String version) {
        this.version = version;
        updateModifiedTime();
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
        updateModifiedTime();
    }

    public long getCreatedTime() { return createdTime; }
    public long getModifiedTime() { return modifiedTime; }

    private void updateModifiedTime() {
        this.modifiedTime = System.currentTimeMillis();
    }
}
