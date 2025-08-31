package net.modularmods.ui;

import imgui.ImGui;
import lombok.Getter;
import lombok.Setter;
import net.modularmods.model.Asset;
import net.modularmods.model.ModelEvent;
import net.modularmods.model.ModelObserver;
import net.modularmods.service.AssetService;
import net.modularmods.service.ProjectService;
import java.util.List;

/**
 * Asset explorer panel for browsing project assets
 */
@Getter
@Setter
public class AssetExplorer implements ModelObserver {
    private final AssetService assetService;
    private final ProjectService projectService;

    public AssetExplorer(AssetService assetService, ProjectService projectService) {
        this.assetService = assetService;
        this.projectService = projectService;
    }

    public void render() {
        if (ImGui.begin("Asset Explorer")) {
            if (projectService.getCurrentProject() == null) {
                ImGui.text("No project loaded");
                ImGui.end();
                return;
            }

            ImGui.text("Project: " + projectService.getCurrentProject().getName());
            ImGui.separator();

            // Import button
            if (ImGui.button("Import Asset")) {
                // TODO: Show file dialog for importing assets
            }

            ImGui.separator();

            // Asset categories
            renderAssetCategory("Textures", Asset.Type.TEXTURE);
            renderAssetCategory("Models", Asset.Type.MODEL);
            renderAssetCategory("Sounds", Asset.Type.SOUND);
            renderAssetCategory("Scripts", Asset.Type.SCRIPT);
        }
        ImGui.end();
    }

    private void renderAssetCategory(String categoryName, Asset.Type type) {
        if (ImGui.treeNode(categoryName)) {
            List<Asset> assets = assetService.getAssetsByType(type);

            if (assets.isEmpty()) {
                ImGui.text("No " + categoryName.toLowerCase() + " found");
            } else {
                for (Asset asset : assets) {
                    boolean selected = asset.equals(assetService.getSelectedAsset());

                    if (ImGui.selectable(asset.getName(), selected)) {
                        assetService.selectAsset(asset);
                    }

                    // Context menu
                    if (ImGui.beginPopupContextItem()) {
                        if (ImGui.menuItem("Delete")) {
                            assetService.removeAsset(asset);
                        }
                        if (ImGui.menuItem("Rename")) {
                            // TODO: Implement rename functionality
                        }
                        ImGui.endPopup();
                    }
                }
            }
            ImGui.treePop();
        }
    }

    @Override
    public void onModelChanged(ModelEvent event) {
        // React to model changes if needed
        // This will be called when assets are added/removed/selected
    }
}
