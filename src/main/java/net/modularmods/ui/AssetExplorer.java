package net.modularmods.ui;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImString;
import net.modularmods.mvc.Model;
import net.modularmods.model.Asset;
import net.modularmods.model.GunPack;

public class AssetExplorer {
    private boolean showImportDialog = false;
    private final ImString importPathBuffer = new ImString(256);
    private Asset.Type selectedAssetType = Asset.Type.MODEL;

    public AssetExplorer() {
        importPathBuffer.set("");
    }

    public void render(Model model) {
        ImGui.text("Asset Explorer");

        if (ImGui.button("Import Asset")) {
            showImportDialog = true;
        }

        ImGui.separator();

        GunPack selectedPack = model.getSelectedPack();
        if (selectedPack == null) {
            ImGui.text("No pack selected");
            return;
        }

        ImGui.text("Assets in: " + selectedPack.getName());

        // Group assets by type
        for (Asset.Type type : Asset.Type.values()) {
            boolean hasAssetsOfType = selectedPack.getAssets().stream()
                    .anyMatch(asset -> asset.getType() == type);

            if (hasAssetsOfType) {
                if (ImGui.treeNodeEx(type.getDisplayName(), ImGuiTreeNodeFlags.DefaultOpen)) {
                    for (Asset asset : selectedPack.getAssets()) {
                        if (asset.getType() == type) {
                            int flags = ImGuiTreeNodeFlags.Leaf | ImGuiTreeNodeFlags.NoTreePushOnOpen;
                            if (asset == model.getSelectedAsset()) {
                                flags |= ImGuiTreeNodeFlags.Selected;
                            }

                            ImGui.treeNodeEx(asset.getName() + "##" + asset.getId(), flags);

                            if (ImGui.isItemClicked()) {
                                // TODO: Call controller to select asset
                            }

                            // Context menu for assets
                            if (ImGui.beginPopupContextItem()) {
                                if (ImGui.menuItem("Remove Asset")) {
                                    // TODO: Call controller to remove asset
                                }
                                ImGui.endPopup();
                            }
                        }
                    }
                    ImGui.treePop();
                }
            }
        }

        // Import dialog
        if (showImportDialog) {
            renderImportDialog(model);
        }
    }

    private void renderImportDialog(Model model) {
        ImGui.openPopup("Import Asset");

        if (ImGui.beginPopupModal("Import Asset")) {
            ImGui.text("Import a new asset");

            ImGui.inputText("File Path", importPathBuffer);

            // Asset type selection
            ImGui.text("Asset Type:");
            for (Asset.Type type : Asset.Type.values()) {
                if (ImGui.radioButton(type.getDisplayName(), selectedAssetType == type)) {
                    selectedAssetType = type;
                }
            }

            if (ImGui.button("Import")) {
                if (!importPathBuffer.get().trim().isEmpty()) {
                    // TODO: Call controller to import asset
                    resetImportDialog();
                    showImportDialog = false;
                    ImGui.closeCurrentPopup();
                }
            }

            ImGui.sameLine();
            if (ImGui.button("Browse")) {
                // TODO: Open file dialog
            }

            ImGui.sameLine();
            if (ImGui.button("Cancel")) {
                resetImportDialog();
                showImportDialog = false;
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }
    }

    private void resetImportDialog() {
        importPathBuffer.set("");
        selectedAssetType = Asset.Type.MODEL;
    }
}
