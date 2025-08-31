package net.modularmods.service;

import lombok.Getter;
import net.modularmods.model.Asset;
import net.modularmods.model.ModelEvent;
import net.modularmods.model.Project;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing assets
 */
@Getter
public class AssetService {
    private final ProjectService projectService;
    private Asset selectedAsset;

    public AssetService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public Asset importAsset(String filePath, Asset.Type type) throws IOException {
        Project currentProject = projectService.getCurrentProject();
        if (currentProject == null) {
            throw new IllegalStateException("No project loaded");
        }

        Path sourcePath = Paths.get(filePath);
        String fileName = sourcePath.getFileName().toString();

        // Determine target directory based on asset type
        String targetDir = getAssetDirectoryForType(type);
        Path targetPath = projectService.getProjectDirectory().resolve("assets").resolve(targetDir).resolve(fileName);

        // Copy file to project assets
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Create asset model
        Asset asset = new Asset(fileName, type, targetPath.toString());

        // Add to project
        currentProject.addAsset(asset);

        return asset;
    }

    public void removeAsset(Asset asset) {
        Project currentProject = projectService.getCurrentProject();
        if (currentProject == null) {
            return;
        }

        currentProject.removeAsset(asset);

        // Optionally delete file
        try {
            Files.deleteIfExists(Paths.get(asset.getFilePath()));
        } catch (IOException e) {
            System.err.println("Failed to delete asset file: " + e.getMessage());
        }
    }

    public void selectAsset(Asset asset) {
        this.selectedAsset = asset;
        Project currentProject = projectService.getCurrentProject();
        if (currentProject != null) {
            currentProject.notifyObservers(new ModelEvent(ModelEvent.Type.ASSET_SELECTED, asset));
        }
    }

    public List<Asset> getAssetsByType(Asset.Type type) {
        Project currentProject = projectService.getCurrentProject();
        if (currentProject == null) {
            return new ArrayList<>();
        }

        return currentProject.getAssets().stream()
                .filter(asset -> asset.getType() == type)
                .collect(Collectors.toList());
    }

    private String getAssetDirectoryForType(Asset.Type type) {
        switch (type) {
            case TEXTURE: return "textures";
            case MODEL: return "models";
            case SOUND: return "sounds";
            case ANIMATION: return "animations";
            case SCRIPT: return "scripts";
            case CONFIG: return "configs";
            default: return "misc";
        }
    }
}
