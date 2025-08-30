package net.modularmods.ui;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class StatusBar {

    public void render(String statusMessage) {
        // Position at bottom of screen
        ImGui.setNextWindowPos(0, ImGui.getIO().getDisplaySizeY() - 25);
        ImGui.setNextWindowSize(ImGui.getIO().getDisplaySizeX(), 25);

        int flags = ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize |
                   ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoScrollbar |
                   ImGuiWindowFlags.NoSavedSettings;

        if (ImGui.begin("StatusBar", flags)) {
            ImGui.text(statusMessage);
        }
        ImGui.end();
    }
}
