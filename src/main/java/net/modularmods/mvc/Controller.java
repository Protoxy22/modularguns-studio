package net.modularmods.mvc;

import imgui.ImGui;
import net.modularmods.model.*;
import org.lwjgl.glfw.GLFW;

public class Controller {
    private final Model model;
    private final View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void update() {
        handleInput();
        updateModel();
    }

    private void handleInput() {
        // Handle keyboard shortcuts
        ImGui.getIO();

        // Ctrl+N - New Project
        if (ImGui.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) && ImGui.isKeyPressed(GLFW.GLFW_KEY_N)) {
            createNewProject();
        }

        // Ctrl+O - Open Project
        if (ImGui.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) && ImGui.isKeyPressed(GLFW.GLFW_KEY_O)) {
            loadProject();
        }

        // Ctrl+S - Save Project
        if (ImGui.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) && ImGui.isKeyPressed(GLFW.GLFW_KEY_S)) {
            saveProject();
        }
    }

    private void updateModel() {
        // Update any time-based model changes here
    }

    // Project Management Actions
    public void createNewProject() {
        // For now, create a default project - later add file dialog
        model.createNewProject("New Project", System.getProperty("user.home"));
    }

    public void loadProject() {
        // TODO: Implement file dialog for project loading
        // For now, just create a sample project with test data
        model.createNewProject("Sample Project", System.getProperty("user.home"));
        createSampleData();
    }

    public void saveProject() {
        model.saveProject();
    }

    // Gun Pack Management Actions
    public void createGunPack(String name, String description, String version) {
        model.createGunPack(name, description, version);
    }

    public void selectGunPack(GunPack pack) {
        model.setSelectedPack(pack);
    }

    public void deleteGunPack(GunPack pack) {
        model.deleteGunPack(pack);
    }

    // Gun Management Actions
    public void createGun(String name) {
        model.createGun(name);
    }

    public void selectGun(Gun gun) {
        model.setSelectedGun(gun);
    }

    public void deleteGun(Gun gun) {
        model.deleteGun(gun);
    }

    // Asset Management Actions
    public void importAsset(String filePath, Asset.Type type) {
        model.importAsset(filePath, type);
    }

    public void selectAsset(Asset asset) {
        model.setSelectedAsset(asset);
    }

    // Export Actions
    public void exportGunPack(GunPack pack) {
        try {
            model.getExportService().exportPack(pack, model.getCurrentProject().getPath() + "/exports");
        } catch (Exception e) {
            System.err.println("Export failed: " + e.getMessage());
        }
    }

    // Helper method to create sample data for testing
    private void createSampleData() {
        // Create sample gun pack
        model.createGunPack("Assault Rifles", "Collection of assault rifles", "1.0.0");

        if (model.getSelectedPack() != null) {
            // Create sample guns
            model.createGun("AK-47");
            model.createGun("M4A1");
            model.createGun("SCAR-L");
        }
    }

    public void cleanup() {
        // Cleanup any controller-specific resources
    }
}
