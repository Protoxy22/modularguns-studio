package net.modularmods.service;

import net.modularmods.model.Asset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class AssetService {

    public Asset importAsset(String filePath, Asset.Type type) throws IOException {
        // Validate file exists
        Path sourcePath = Paths.get(filePath);
        if (!Files.exists(sourcePath)) {
            throw new IOException("File not found: " + filePath);
        }

        // Validate file extension
        String extension = getFileExtension(sourcePath.getFileName().toString()).toLowerCase();
        boolean validExtension = false;
        for (String validExt : type.getExtensions()) {
            if (extension.equals(validExt.toLowerCase())) {
                validExtension = true;
                break;
            }
        }

        if (!validExtension) {
            throw new IOException("Invalid file extension for " + type.getDisplayName() + ": " + extension);
        }

        // Create asset
        Asset asset = new Asset(filePath, type);

        // TODO: Copy file to project assets directory
        // TODO: Load and validate asset content

        return asset;
    }

    public void copyAssetToProject(Asset asset, String projectPath) throws IOException {
        Path sourcePath = Paths.get(asset.getFilePath());
        Path targetDir = Paths.get(projectPath, "assets", asset.getType().name().toLowerCase());

        // Create target directory if it doesn't exist
        Files.createDirectories(targetDir);

        Path targetPath = targetDir.resolve(asset.getFileName());
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    public boolean validateAsset(Asset asset) {
        // TODO: Implement asset validation based on type
        Path path = Paths.get(asset.getFilePath());
        return Files.exists(path) && Files.isRegularFile(path);
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex) : "";
    }
}
