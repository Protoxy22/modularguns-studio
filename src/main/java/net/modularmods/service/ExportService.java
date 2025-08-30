package net.modularmods.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.modularmods.model.GunPack;
import net.modularmods.model.Gun;
import net.modularmods.model.Asset;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ExportService {
    private final Gson gson;

    public ExportService() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public void exportPack(GunPack pack, String exportPath) throws IOException {
        // Create export directory
        Path packExportPath = Paths.get(exportPath, pack.getName().replaceAll("[^a-zA-Z0-9]", "_"));
        Files.createDirectories(packExportPath);

        // Export pack metadata
        exportPackMetadata(pack, packExportPath);

        // Export guns data
        exportGunsData(pack, packExportPath);

        // Copy assets
        exportAssets(pack, packExportPath);

        System.out.println("Pack exported successfully to: " + packExportPath);
    }

    private void exportPackMetadata(GunPack pack, Path exportPath) throws IOException {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("id", pack.getId());
        metadata.put("name", pack.getName());
        metadata.put("description", pack.getDescription());
        metadata.put("version", pack.getVersion());
        metadata.put("gunCount", pack.getGuns().size());
        metadata.put("assetCount", pack.getAssets().size());

        File metadataFile = exportPath.resolve("pack.json").toFile();
        try (FileWriter writer = new FileWriter(metadataFile)) {
            gson.toJson(metadata, writer);
        }
    }

    private void exportGunsData(GunPack pack, Path exportPath) throws IOException {
        Path gunsDir = exportPath.resolve("guns");
        Files.createDirectories(gunsDir);

        for (Gun gun : pack.getGuns()) {
            Map<String, Object> gunData = new HashMap<>();
            gunData.put("id", gun.getId());
            gunData.put("name", gun.getName());
            gunData.put("description", gun.getDescription());
            gunData.put("stats", gun.getStats());

            // Add asset references
            Map<String, String> assetRefs = new HashMap<>();
            if (gun.getModelAsset() != null) assetRefs.put("model", gun.getModelAsset().getFileName());
            if (gun.getTextureAsset() != null) assetRefs.put("texture", gun.getTextureAsset().getFileName());
            if (gun.getAnimationAsset() != null) assetRefs.put("animation", gun.getAnimationAsset().getFileName());
            if (gun.getIconAsset() != null) assetRefs.put("icon", gun.getIconAsset().getFileName());
            gunData.put("assets", assetRefs);

            File gunFile = gunsDir.resolve(gun.getName().replaceAll("[^a-zA-Z0-9]", "_") + ".json").toFile();
            try (FileWriter writer = new FileWriter(gunFile)) {
                gson.toJson(gunData, writer);
            }
        }
    }

    private void exportAssets(GunPack pack, Path exportPath) throws IOException {
        Path assetsDir = exportPath.resolve("assets");
        Files.createDirectories(assetsDir);

        for (Asset asset : pack.getAssets()) {
            Path sourceFile = Paths.get(asset.getFilePath());
            Path targetFile = assetsDir.resolve(asset.getFileName());

            if (Files.exists(sourceFile)) {
                Files.copy(sourceFile, targetFile);
            }
        }
    }
}
