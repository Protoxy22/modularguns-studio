package net.modularmods.ui;

import imgui.ImGui;
import imgui.ImVec2;
import net.modularmods.mvc.Model;

public class Viewport3D {
    private int lastWidth = 0;
    private int lastHeight = 0;

    public void render(Model model) {
        ImGui.text("3D Viewport");

        // Get available content region
        ImVec2 contentRegion = ImGui.getContentRegionAvail();
        int width = (int) contentRegion.x;
        int height = (int) contentRegion.y - 30; // Leave space for controls

        if (width > 0 && height > 0) {
            // Resize framebuffer if needed
            if (width != lastWidth || height != lastHeight) {
                model.getRenderService().resizeFramebuffer(width, height);
                lastWidth = width;
                lastHeight = height;
            }

            // Render to framebuffer
            model.getRenderService().renderToFramebuffer();

            // Display the rendered texture
            int textureId = model.getRenderService().getColorTexture();
            ImGui.image(textureId, width, height, 0, 1, 1, 0); // Flip Y coordinate
        }

        // Viewport controls
        ImGui.separator();
        if (ImGui.button("Reset View")) {
            // TODO: Reset camera
        }
        ImGui.sameLine();
        if (ImGui.button("Fit to View")) {
            // TODO: Fit model to view
        }
        ImGui.sameLine();
        ImGui.text("Rendering: Triangle Demo");
    }

    public void cleanup() {
        // Cleanup resources if needed
    }
}
