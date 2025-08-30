package net.modularmods.ui;

import imgui.ImGui;
import imgui.type.ImString;
import net.modularmods.mvc.Model;
import net.modularmods.model.Gun;
import net.modularmods.model.GunPack;
import net.modularmods.model.Asset;

import java.util.Map;

public class PropertyPanel {
    private final float[] floatBuffer = new float[1];
    private final ImString stringBuffer = new ImString(256);

    public void render(Model model) {
        ImGui.text("Properties");
        ImGui.separator();

        // Show properties based on selection
        GunPack selectedPack = model.getSelectedPack();
        Gun selectedGun = model.getSelectedGun();
        Asset selectedAsset = model.getSelectedAsset();

        if (selectedGun != null) {
            renderGunProperties(selectedGun);
        } else if (selectedAsset != null) {
            renderAssetProperties(selectedAsset);
        } else if (selectedPack != null) {
            renderPackProperties(selectedPack);
        } else {
            ImGui.text("No item selected");
        }
    }

    private void renderGunProperties(Gun gun) {
        ImGui.text("Gun: " + gun.getName());
        ImGui.separator();

        // Basic properties
        stringBuffer.set(gun.getDescription());
        if (ImGui.inputTextMultiline("Description", stringBuffer)) {
            gun.setDescription(stringBuffer.get());
        }

        ImGui.separator();
        ImGui.text("Statistics");

        // Render gun stats
        Map<String, Object> stats = gun.getStats();
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Float || value instanceof Double) {
                floatBuffer[0] = ((Number) value).floatValue();
                if (ImGui.dragFloat(capitalize(key), floatBuffer, 0.1f)) {
                    gun.setStat(key, floatBuffer[0]);
                }
            }
        }

        ImGui.separator();
        ImGui.text("Assets");

        // Asset assignments
        if (gun.getModelAsset() != null) {
            ImGui.text("Model: " + gun.getModelAsset().getName());
        } else {
            ImGui.text("Model: None");
        }

        if (gun.getTextureAsset() != null) {
            ImGui.text("Texture: " + gun.getTextureAsset().getName());
        } else {
            ImGui.text("Texture: None");
        }

        if (gun.getAnimationAsset() != null) {
            ImGui.text("Animation: " + gun.getAnimationAsset().getName());
        } else {
            ImGui.text("Animation: None");
        }

        if (gun.getIconAsset() != null) {
            ImGui.text("Icon: " + gun.getIconAsset().getName());
        } else {
            ImGui.text("Icon: None");
        }
    }

    private void renderAssetProperties(Asset asset) {
        ImGui.text("Asset: " + asset.getName());
        ImGui.separator();

        ImGui.text("Type: " + asset.getType().getDisplayName());
        ImGui.text("File: " + asset.getFileName());
        ImGui.text("Path: " + asset.getFilePath());
        ImGui.text("Extension: " + asset.getFileExtension());
        ImGui.text("Loaded: " + (asset.isLoaded() ? "Yes" : "No"));

        if (ImGui.button("Reload Asset")) {
            // TODO: Implement asset reloading
        }
    }

    private void renderPackProperties(GunPack pack) {
        ImGui.text("Gun Pack: " + pack.getName());
        ImGui.separator();

        stringBuffer.set(pack.getDescription());
        if (ImGui.inputTextMultiline("Description", stringBuffer)) {
            pack.setDescription(stringBuffer.get());
        }


        stringBuffer.set(pack.getVersion());
        if (ImGui.inputText("Version", stringBuffer)) {
            pack.setVersion(stringBuffer.get());
        }

        ImGui.separator();
        ImGui.text("Statistics");
        ImGui.text("Guns: " + pack.getGuns().size());
        ImGui.text("Assets: " + pack.getAssets().size());
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
