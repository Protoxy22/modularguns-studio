package net.modularmods.ui;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import net.modularmods.mvc.Model;
import net.modularmods.mvc.View;
import net.modularmods.model.GunPack;
import net.modularmods.model.Gun;

public class MenuBar {

    public void render(View view) {
        if (ImGui.beginMenuBar()) {
            if (ImGui.beginMenu("File")) {
                if (ImGui.menuItem("New Project", "Ctrl+N")) {
                    view.onNewProject();
                }
                if (ImGui.menuItem("Open Project", "Ctrl+O")) {
                    view.onLoadProject();
                }
                ImGui.separator();
                if (ImGui.menuItem("Save Project", "Ctrl+S")) {
                    view.onSaveProject();
                }
                ImGui.separator();
                if (ImGui.menuItem("Export Pack")) {
                    view.onExportPack();
                }
                ImGui.separator();
                if (ImGui.menuItem("Exit")) {
                    System.exit(0);
                }
                ImGui.endMenu();
            }

            if (ImGui.beginMenu("View")) {
                if (ImGui.menuItem("Show ImGui Demo")) {
                    view.showDemoWindow(true);
                }
                ImGui.endMenu();
            }

            if (ImGui.beginMenu("Help")) {
                if (ImGui.menuItem("About")) {
                    view.showAboutWindow(true);
                }
                ImGui.endMenu();
            }

            ImGui.endMenuBar();
        }
    }
}
