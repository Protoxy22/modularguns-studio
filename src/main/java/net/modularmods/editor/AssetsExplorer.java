package net.modularmods.editor;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import net.modularmods.data.GunPack;

import java.io.File;
import java.util.List;

public class AssetsExplorer {
    private ContentPackManager packManager;
    private String selectedAsset = null;
    private String selectedAssetType = null;

    public void render() {
        ImGui.begin("Assets Explorer", ImGuiWindowFlags.NoCollapse);

        GunPack currentPack = getCurrentPack();
        if (currentPack == null) {
            ImGui.text("No pack loaded. Create or load a pack to view assets.");
            ImGui.end();
            return;
        }

        // Asset categories
        if (ImGui.collapsingHeader("Models", ImGuiTreeNodeFlags.DefaultOpen)) {
            renderAssetCategory("Models", currentPack.getModels());
        }

        if (ImGui.collapsingHeader("Textures", ImGuiTreeNodeFlags.DefaultOpen)) {
            renderAssetCategory("Textures", currentPack.getTextures());
        }

        if (ImGui.collapsingHeader("Animations", ImGuiTreeNodeFlags.DefaultOpen)) {
            renderAssetCategory("Animations", currentPack.getAnimations());
        }

        if (ImGui.collapsingHeader("Stats/JSON", ImGuiTreeNodeFlags.DefaultOpen)) {
            renderAssetCategory("Stats", currentPack.getStats());
        }

        if (ImGui.collapsingHeader("Icons", ImGuiTreeNodeFlags.DefaultOpen)) {
            renderAssetCategory("Icons", currentPack.getIcons());
        }

        ImGui.separator();

        // Asset import section
        ImGui.text("Import Assets:");
        if (ImGui.button("Import Model")) {
            importAsset("Model");
        }
        ImGui.sameLine();
        if (ImGui.button("Import Texture")) {
            importAsset("Texture");
        }
        ImGui.sameLine();
        if (ImGui.button("Import Animation")) {
            importAsset("Animation");
        }

        if (ImGui.button("Import JSON Stats")) {
            importAsset("Stats");
        }
        ImGui.sameLine();
        if (ImGui.button("Import Icon")) {
            importAsset("Icon");
        }

        ImGui.end();
    }

    private void renderAssetCategory(String categoryName, List<String> assets) {
        for (String asset : assets) {
            boolean isSelected = asset.equals(selectedAsset) && categoryName.equals(selectedAssetType);

            if (ImGui.selectable(asset, isSelected)) {
                selectedAsset = asset;
                selectedAssetType = categoryName;
            }

            // Context menu for assets
            if (ImGui.beginPopupContextItem()) {
                if (ImGui.menuItem("Remove")) {
                    removeAsset(categoryName, asset);
                }
                if (ImGui.menuItem("Rename")) {
                    renameAsset(categoryName, asset);
                }
                if (ImGui.menuItem("Properties")) {
                    showAssetProperties(categoryName, asset);
                }
                ImGui.endPopup();
            }
        }

        if (assets.isEmpty()) {
            ImGui.textDisabled("No " + categoryName.toLowerCase() + " imported");
        }
    }

    private void importAsset(String assetType) {
        // TODO: Implement file dialog for asset import
        System.out.println("Import " + assetType + " - TODO: File dialog");

        // For now, add a placeholder asset
        GunPack currentPack = getCurrentPack();
        if (currentPack != null) {
            String placeholder = "placeholder_" + assetType.toLowerCase() + "_" + System.currentTimeMillis();
            switch (assetType) {
                case "Model":
                    currentPack.getModels().add(placeholder + ".obj");
                    break;
                case "Texture":
                    currentPack.getTextures().add(placeholder + ".png");
                    break;
                case "Animation":
                    currentPack.getAnimations().add(placeholder + ".anim");
                    break;
                case "Stats":
                    currentPack.getStats().add(placeholder + ".json");
                    break;
                case "Icon":
                    currentPack.getIcons().add(placeholder + ".png");
                    break;
            }
        }
    }

    private void removeAsset(String categoryName, String asset) {
        GunPack currentPack = getCurrentPack();
        if (currentPack != null) {
            switch (categoryName) {
                case "Models":
                    currentPack.getModels().remove(asset);
                    break;
                case "Textures":
                    currentPack.getTextures().remove(asset);
                    break;
                case "Animations":
                    currentPack.getAnimations().remove(asset);
                    break;
                case "Stats":
                    currentPack.getStats().remove(asset);
                    break;
                case "Icons":
                    currentPack.getIcons().remove(asset);
                    break;
            }

            if (asset.equals(selectedAsset)) {
                selectedAsset = null;
                selectedAssetType = null;
            }
        }
    }

    private void renameAsset(String categoryName, String asset) {
        // TODO: Implement asset renaming dialog
        System.out.println("Rename " + asset + " - TODO");
    }

    private void showAssetProperties(String categoryName, String asset) {
        // TODO: Implement asset properties dialog
        System.out.println("Show properties for " + asset + " - TODO");
    }

    private GunPack getCurrentPack() {
        // Get current pack from the pack manager
        return packManager != null ? packManager.getCurrentPack() : null;
    }

    public String getSelectedAsset() {
        return selectedAsset;
    }

    public String getSelectedAssetType() {
        return selectedAssetType;
    }

    public void setPackManager(ContentPackManager packManager) {
        this.packManager = packManager;
    }
}
