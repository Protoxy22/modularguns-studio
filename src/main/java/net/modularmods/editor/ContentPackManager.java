package net.modularmods.editor;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import net.modularmods.data.GunPack;
import net.modularmods.data.PackMetadata;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContentPackManager {
    private List<GunPack> loadedPacks = new ArrayList<>();
    private GunPack currentPack = null;
    private ImString newPackName = new ImString(256);
    private ImString newPackDescription = new ImString(512);
    private ImString newPackVersion = new ImString("1.0.0", 64);
    private boolean showCreatePackDialog = false;

    public void render() {
        ImGui.begin("Content Pack Manager", ImGuiWindowFlags.NoCollapse);

        // Toolbar
        if (ImGui.button("New Pack")) {
            showCreatePackDialog = true;
            newPackName.set("");
            newPackDescription.set("");
            newPackVersion.set("1.0.0");
        }
        ImGui.sameLine();
        if (ImGui.button("Load Pack")) {
            loadPack();
        }
        ImGui.sameLine();
        if (ImGui.button("Save Pack") && currentPack != null) {
            savePack();
        }
        ImGui.sameLine();
        if (ImGui.button("Export Pack") && currentPack != null) {
            exportPack();
        }

        ImGui.separator();

        // Current pack info
        if (currentPack != null) {
            ImGui.text("Current Pack: " + currentPack.getMetadata().getName());
            ImGui.text("Version: " + currentPack.getMetadata().getVersion());
            ImGui.text("Description: " + currentPack.getMetadata().getDescription());
            ImGui.separator();
        }

        // Loaded packs list
        ImGui.text("Loaded Packs:");
        for (int i = 0; i < loadedPacks.size(); i++) {
            GunPack pack = loadedPacks.get(i);
            boolean isSelected = pack == currentPack;

            if (ImGui.selectable(pack.getMetadata().getName(), isSelected)) {
                currentPack = pack;
            }

            if (ImGui.isItemHovered()) {
                ImGui.beginTooltip();
                ImGui.text("Version: " + pack.getMetadata().getVersion());
                ImGui.text("Description: " + pack.getMetadata().getDescription());
                ImGui.text("Models: " + pack.getModels().size());
                ImGui.text("Textures: " + pack.getTextures().size());
                ImGui.endTooltip();
            }
        }

        // Create pack dialog
        if (showCreatePackDialog) {
            renderCreatePackDialog();
        }

        ImGui.end();
    }

    private void renderCreatePackDialog() {
        ImGui.openPopup("Create New Pack");

        if (ImGui.beginPopupModal("Create New Pack")) {
            ImGui.text("Create a new gun pack:");
            ImGui.separator();

            ImGui.text("Pack Name:");
            ImGui.inputText("##PackName", newPackName);

            ImGui.text("Version:");
            ImGui.inputText("##PackVersion", newPackVersion);

            ImGui.text("Description:");
            ImGui.inputTextMultiline("##PackDescription", newPackDescription, 200, 100);

            ImGui.separator();

            if (ImGui.button("Create")) {
                if (!newPackName.get().isEmpty()) {
                    createNewPack();
                    showCreatePackDialog = false;
                }
            }
            ImGui.sameLine();
            if (ImGui.button("Cancel")) {
                showCreatePackDialog = false;
            }

            ImGui.endPopup();
        }
    }

    private void createNewPack() {
        PackMetadata metadata = new PackMetadata(
            newPackName.get(),
            newPackVersion.get(),
            newPackDescription.get()
        );

        GunPack newPack = new GunPack(metadata);
        loadedPacks.add(newPack);
        currentPack = newPack;
    }

    private void loadPack() {
        // TODO: Implement file dialog for loading packs
        System.out.println("Load pack functionality - TODO");
    }

    private void savePack() {
        // TODO: Implement pack saving
        System.out.println("Save pack functionality - TODO");
    }

    private void exportPack() {
        // TODO: Implement pack export
        System.out.println("Export pack functionality - TODO");
    }

    public GunPack getCurrentPack() {
        return currentPack;
    }
}
