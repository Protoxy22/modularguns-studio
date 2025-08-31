package net.modularmods.ui;

import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import lombok.Getter;
import lombok.Setter;
import net.modularmods.model.Asset;
import net.modularmods.model.Gun;
import net.modularmods.model.ModelEvent;
import net.modularmods.model.ModelObserver;
import net.modularmods.service.AssetService;

/**
 * Property panel for editing selected objects
 */
@Getter
@Setter
public class PropertyPanel implements ModelObserver {
    private final AssetService assetService;
    private Object selectedObject;

    public PropertyPanel(AssetService assetService) {
        this.assetService = assetService;
    }

    public void render() {
        if (ImGui.begin("Properties")) {
            selectedObject = assetService.getSelectedAsset();

            if (selectedObject == null) {
                ImGui.text("No object selected");
            } else {
                renderObjectProperties();
            }
        }
        ImGui.end();
    }

    private void renderObjectProperties() {
        if (selectedObject instanceof Asset) {
            renderAssetProperties((Asset) selectedObject);
        } else if (selectedObject instanceof Gun) {
            renderGunProperties((Gun) selectedObject);
        }
    }

    private void renderAssetProperties(Asset asset) {
        ImGui.text("Asset Properties");
        ImGui.separator();

        // Name
        ImGui.text("Name: " + asset.getName());
        ImGui.text("Type: " + asset.getType().toString());
        ImGui.text("File Path: " + asset.getFilePath());
        ImGui.text("File Size: " + formatFileSize(asset.getFileSize()));
        ImGui.text("Created: " + asset.getCreatedAt().toString());
        ImGui.text("Modified: " + asset.getModifiedAt().toString());

        // Description
        ImString description = new ImString(asset.getDescription() != null ? asset.getDescription() : "", 256);
        if (ImGui.inputTextMultiline("Description", description)) {
            asset.setDescription(description.get());
            asset.updateModifiedTime();
        }
    }

    private void renderGunProperties(Gun gun) {
        ImGui.text("Gun Properties");
        ImGui.separator();

        // Name
        ImString name = new ImString(gun.getName(), 256);
        if (ImGui.inputText("Name", name)) {
            gun.setName(name.get());
        }

        // Display Name
        ImString displayName = new ImString(gun.getDisplayName(), 256);
        if (ImGui.inputText("Display Name", displayName)) {
            gun.setDisplayName(displayName.get());
        }

        // Damage
        float[] damage = {gun.getDamage()};
        if (ImGui.sliderFloat("Damage", damage, 0.0f, 200.0f)) {
            gun.setDamage(damage[0]);
        }

        // Range
        float[] range = {gun.getRange()};
        if (ImGui.sliderFloat("Range", range, 0.0f, 500.0f)) {
            gun.setRange(range[0]);
        }

        // Fire Rate
        float[] fireRate = {gun.getFireRate()};
        if (ImGui.sliderFloat("Fire Rate (RPM)", fireRate, 100.0f, 2000.0f)) {
            gun.setFireRate(fireRate[0]);
        }

        // Automatic
        ImBoolean isAutomatic = new ImBoolean(gun.isAutomatic());
        if (ImGui.checkbox("Automatic", isAutomatic)) {
            gun.setAutomatic(isAutomatic.get());
        }

        // Asset paths
        ImGui.separator();
        ImGui.text("Asset References");

        ImString modelPath = new ImString(gun.getModelPath() != null ? gun.getModelPath() : "", 256);
        if (ImGui.inputText("Model Path", modelPath)) {
            gun.setModelPath(modelPath.get());
        }

        ImString texturePath = new ImString(gun.getTexturePath() != null ? gun.getTexturePath() : "", 256);
        if (ImGui.inputText("Texture Path", texturePath)) {
            gun.setTexturePath(texturePath.get());
        }

        ImString soundPath = new ImString(gun.getSoundPath() != null ? gun.getSoundPath() : "", 256);
        if (ImGui.inputText("Sound Path", soundPath)) {
            gun.setSoundPath(soundPath.get());
        }
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }

    @Override
    public void onModelChanged(ModelEvent event) {
        if (event.getType() == ModelEvent.Type.ASSET_SELECTED) {
            selectedObject = event.getData();
        }
    }
}
