package net.modularmods.ui;

import imgui.ImGui;
import lombok.Getter;
import lombok.Setter;
import net.modularmods.service.ProjectService;

/**
 * Menu bar component for the main application
 */
@Getter
@Setter
public class MenuBar {
    private final ProjectService projectService;
    private boolean showAboutDialog = false;

    public MenuBar(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void render() {
        if (ImGui.beginMainMenuBar()) {
            renderFileMenu();
            renderEditMenu();
            renderViewMenu();
            renderHelpMenu();
            ImGui.endMainMenuBar();
        }

        // Render dialogs
        if (showAboutDialog) {
            renderAboutDialog();
        }
    }

    private void renderFileMenu() {
        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("New Project", "Ctrl+N")) {
                // TODO: Show new project dialog
            }
            if (ImGui.menuItem("Open Project", "Ctrl+O")) {
                // TODO: Show open project dialog
            }
            if (ImGui.menuItem("Save Project", "Ctrl+S")) {
                try {
                    if (projectService.getCurrentProject() != null) {
                        projectService.saveProject();
                    }
                } catch (Exception e) {
                    System.err.println("Failed to save project: " + e.getMessage());
                }
            }
            if (ImGui.menuItem("Save As...", "Ctrl+Shift+S")) {
                // TODO: Show save as dialog
            }
            ImGui.separator();
            if (ImGui.menuItem("Exit", "Alt+F4")) {
                // TODO: Signal application to close
            }
            ImGui.endMenu();
        }
    }

    private void renderEditMenu() {
        if (ImGui.beginMenu("Edit")) {
            if (ImGui.menuItem("Undo", "Ctrl+Z")) {
                // TODO: Implement undo functionality
            }
            if (ImGui.menuItem("Redo", "Ctrl+Y")) {
                // TODO: Implement redo functionality
            }
            ImGui.separator();
            if (ImGui.menuItem("Preferences", "Ctrl+,")) {
                // TODO: Show preferences dialog
            }
            ImGui.endMenu();
        }
    }

    private void renderViewMenu() {
        if (ImGui.beginMenu("View")) {
            if (ImGui.menuItem("Asset Explorer")) {
                // TODO: Toggle asset explorer visibility
            }
            if (ImGui.menuItem("Properties Panel")) {
                // TODO: Toggle properties panel visibility
            }
            if (ImGui.menuItem("3D Viewport")) {
                // TODO: Toggle viewport visibility
            }
            ImGui.separator();
            if (ImGui.menuItem("Reset Layout")) {
                // TODO: Reset ImGui layout
            }
            ImGui.endMenu();
        }
    }

    private void renderHelpMenu() {
        if (ImGui.beginMenu("Help")) {
            if (ImGui.menuItem("Documentation")) {
                // TODO: Open documentation
            }
            if (ImGui.menuItem("Report Bug")) {
                // TODO: Open bug report page
            }
            ImGui.separator();
            if (ImGui.menuItem("About")) {
                showAboutDialog = true;
            }
            ImGui.endMenu();
        }
    }

    private void renderAboutDialog() {
        if (ImGui.begin("About ModularGuns Studio")) {
            ImGui.text("ModularGuns Studio v1.0.0");
            ImGui.text("A game editor for creating modular gun configurations");
            ImGui.separator();
            ImGui.text("Built with LWJGL3 and ImGui");

            if (ImGui.button("Close")) {
                showAboutDialog = false;
            }
            ImGui.end();
        }
    }
}
