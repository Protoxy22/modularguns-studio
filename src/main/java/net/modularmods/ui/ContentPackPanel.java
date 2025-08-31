package net.modularmods.ui;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImString;
import lombok.Getter;
import lombok.Setter;
import net.modularmods.model.GunPack;
import net.modularmods.model.ModelEvent;
import net.modularmods.model.ModelObserver;
import net.modularmods.service.ProjectService;

import java.util.List;

/**
 * Content Pack Management panel for managing gun packs and content
 */
@Getter
@Setter
public class ContentPackPanel implements ModelObserver {
    private final ProjectService projectService;
    private final ImString newPackName = new ImString(256);
    private boolean showCreateDialog = false;

    public ContentPackPanel(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void render() {
        if (ImGui.begin("Content Packs")) {
            renderToolbar();
            ImGui.separator();
            renderContentPacks();

            if (showCreateDialog) {
                renderCreatePackDialog();
            }
        }
        ImGui.end();
    }

    private void renderToolbar() {
        if (ImGui.button("New Pack")) {
            showCreateDialog = true;
            newPackName.set("");
        }

        ImGui.sameLine();
        if (ImGui.button("Import Pack")) {
            // TODO: Show file dialog for importing content packs
        }

        ImGui.sameLine();
        if (ImGui.button("Export Pack")) {
            // TODO: Export selected pack
        }
    }

    private void renderContentPacks() {
        if (projectService.getCurrentProject() == null) {
            ImGui.textColored(0.7f, 0.7f, 0.7f, 1.0f, "No project loaded");
            return;
        }

        List<GunPack> packs = projectService.getCurrentProject().getGunPacks();

        if (packs.isEmpty()) {
            ImGui.textColored(0.7f, 0.7f, 0.7f, 1.0f, "No content packs found");
            return;
        }

        for (GunPack pack : packs) {
            int flags = ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.OpenOnDoubleClick;

            if (ImGui.treeNodeEx(pack.getName(), flags)) {
                ImGui.indent();

                // Pack info
//                ImGui.text("Version: " + pack.getVersion()); // GunPack doesn't have version field
                ImGui.text("Guns: " + pack.getGuns().size());

                // Gun list
                if (ImGui.treeNode("Guns")) {
                    pack.getGuns().forEach(gun -> {
                        if (ImGui.selectable(gun.getName())) {
                            // TODO: Select gun for editing
                        }

                        if (ImGui.beginPopupContextItem()) {
                            if (ImGui.menuItem("Edit")) {
                                // TODO: Open gun editor
                            }
                            if (ImGui.menuItem("Duplicate")) {
                                // TODO: Duplicate gun
                            }
                            if (ImGui.menuItem("Delete")) {
                                // TODO: Delete gun
                            }
                            ImGui.endPopup();
                        }
                    });
                    ImGui.treePop();
                }

                ImGui.unindent();
                ImGui.treePop();
            }

            // Pack context menu
            if (ImGui.beginPopupContextItem()) {
                if (ImGui.menuItem("Rename")) {
                    // TODO: Rename pack
                }
                if (ImGui.menuItem("Delete")) {
                    // TODO: Delete pack
                }
                if (ImGui.menuItem("Export")) {
                    // TODO: Export pack
                }
                ImGui.endPopup();
            }
        }
    }

    private void renderCreatePackDialog() {
        if (ImGui.beginPopupModal("Create New Content Pack")) {
            ImGui.text("Enter pack name:");
            ImGui.inputText("##packname", newPackName);

            ImGui.separator();

            if (ImGui.button("Create")) {
                if (!newPackName.get().trim().isEmpty()) {
                    // TODO: Create new pack
                    showCreateDialog = false;
                    ImGui.closeCurrentPopup();
                }
            }

            ImGui.sameLine();
            if (ImGui.button("Cancel")) {
                showCreateDialog = false;
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }

        if (showCreateDialog) {
            ImGui.openPopup("Create New Content Pack");
        }
    }

    @Override
    public void onModelChanged(ModelEvent event) {
        // Handle model changes if needed
    }
}
