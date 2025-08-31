package net.modularmods.ui;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import lombok.Getter;
import lombok.Setter;
import net.modularmods.service.RenderService;

/**
 * 3D viewport for rendering and previewing 3D models
 */
@Getter
@Setter
public class Viewport3D {
    private final RenderService renderService;
    private boolean showGrid = true;
    private boolean showAxes = true;

    public Viewport3D(RenderService renderService) {
        this.renderService = renderService;
    }

    public void render() {
        if (ImGui.begin("3D Viewport", ImGuiWindowFlags.MenuBar)) {
            renderMenuBar();
            renderViewport();
        }
        ImGui.end();
    }

    private void renderMenuBar() {
        if (ImGui.beginMenuBar()) {
            if (ImGui.beginMenu("View")) {
                if (ImGui.menuItem("Reset Camera")) {
                    renderService.resetCamera();
                }
                if (ImGui.menuItem("Toggle Wireframe")) {
                    renderService.toggleWireframe();
                }
                ImGui.separator();
                if (ImGui.menuItem("Show Grid", null, showGrid)) {
                    showGrid = !showGrid;
                }
                if (ImGui.menuItem("Show Axes", null, showAxes)) {
                    showAxes = !showAxes;
                }
                ImGui.endMenu();
            }

            if (ImGui.beginMenu("Shading")) {
                if (ImGui.menuItem("Solid")) {
                    renderService.setWireframeMode(false);
                }
                if (ImGui.menuItem("Wireframe")) {
                    renderService.setWireframeMode(true);
                }
                ImGui.endMenu();
            }

            ImGui.endMenuBar();
        }
    }

    private void renderViewport() {
        float viewportWidth = ImGui.getContentRegionAvailX();
        float viewportHeight = ImGui.getContentRegionAvailY();

        if (viewportWidth > 0 && viewportHeight > 0) {
            // TODO: Implement framebuffer rendering for 3D content
            // For now, just show placeholder text
            ImGui.text("3D Viewport");
            ImGui.text(String.format("Size: %.0f x %.0f", viewportWidth, viewportHeight));
            ImGui.separator();

            // Camera controls info
            ImGui.text("Camera Controls:");
            ImGui.text("- Mouse + Drag: Rotate camera");
            ImGui.text("- Scroll: Zoom in/out");
            ImGui.text("- Middle Mouse + Drag: Pan camera");

            // Display camera info
            float[] cameraPos = renderService.getCameraPosition();
            ImGui.text(String.format("Camera Position: (%.2f, %.2f, %.2f)", cameraPos[0], cameraPos[1], cameraPos[2]));

            // Render settings
            ImGui.separator();
            ImGui.text("Render Settings:");
            ImGui.text("Wireframe: " + (renderService.isWireframeMode() ? "ON" : "OFF"));
            ImGui.text("Grid: " + (showGrid ? "ON" : "OFF"));
            ImGui.text("Axes: " + (showAxes ? "ON" : "OFF"));
        }
    }
}
