package net.modularmods.ui;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import net.modularmods.mvc.Model;
import net.modularmods.model.GunPack;
import net.modularmods.model.Gun;
import net.modularmods.model.Project;

public class ProjectPanel {
    private boolean showCreatePackDialog = false;

    private final imgui.type.ImString packNameBuffer = new imgui.type.ImString(64);
    private final imgui.type.ImString packDescBuffer = new imgui.type.ImString(256);
    private final imgui.type.ImString packVersionBuffer = new imgui.type.ImString(32);

    public ProjectPanel() {
        packNameBuffer.set("");
        packDescBuffer.set("");
        packVersionBuffer.set("1.0.0");
    }

    public void render(Model model) {
        Project project = model.getCurrentProject();

        // Project info
        ImGui.text("Project: " + project.getName());
        ImGui.text("Path: " + project.getPath());
        ImGui.separator();

        // Gun Packs tree
        ImGui.text("Gun Packs (" + model.getGunPacks().size() + ")");

        if (ImGui.button("Create Pack")) {
            showCreatePackDialog = true;
        }

        ImGui.separator();

        // Render gun packs
        for (GunPack pack : model.getGunPacks()) {
            int flags = ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.OpenOnDoubleClick;
            if (pack == model.getSelectedPack()) {
                flags |= ImGuiTreeNodeFlags.Selected;
            }

            boolean nodeOpen = ImGui.treeNodeEx(pack.getName() + "##" + pack.getId(), flags);

            if (ImGui.isItemClicked()) {
                // TODO: Call controller to select pack
            }

            if (nodeOpen) {
                // Show guns in pack
                for (Gun gun : pack.getGuns()) {
                    int gunFlags = ImGuiTreeNodeFlags.Leaf | ImGuiTreeNodeFlags.NoTreePushOnOpen;
                    if (gun == model.getSelectedGun()) {
                        gunFlags |= ImGuiTreeNodeFlags.Selected;
                    }

                    ImGui.treeNodeEx(gun.getName() + "##" + gun.getId(), gunFlags);

                    if (ImGui.isItemClicked()) {
                        // TODO: Call controller to select gun
                    }
                }

                ImGui.treePop();
            }
        }

        // Create pack dialog
        if (showCreatePackDialog) {
            renderCreatePackDialog(model);
        }
    }

    private void renderCreatePackDialog(Model model) {
        ImGui.openPopup("Create Gun Pack");

        if (ImGui.beginPopupModal("Create Gun Pack")) {
            ImGui.text("Create a new gun pack");

            ImGui.inputText("Name", packNameBuffer);
            ImGui.inputTextMultiline("Description", packDescBuffer);
            ImGui.inputText("Version", packVersionBuffer);

            if (ImGui.button("Create")) {
                if (!packNameBuffer.get().trim().isEmpty()) {
                    // TODO: Call controller to create pack
                    resetDialogBuffers();
                    showCreatePackDialog = false;
                    ImGui.closeCurrentPopup();
                }
            }

            ImGui.sameLine();
            if (ImGui.button("Cancel")) {
                resetDialogBuffers();
                showCreatePackDialog = false;
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }
    }

    private void resetDialogBuffers() {
        packNameBuffer.set("");
        packDescBuffer.set("");
        packVersionBuffer.set("1.0.0");
    }
}
