package net.modularmods.mvc;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.flag.ImGuiStyleVar;
import net.modularmods.ui.*;
import net.modularmods.model.*;

public class View implements ModelObserver {
    private final Model model;

    // UI Panels
    private final MenuBar menuBar;
    private final ProjectPanel projectPanel;
    private final AssetExplorer assetExplorer;
    private final PropertyPanel propertyPanel;
    private final Viewport3D viewport3D;
    private final StatusBar statusBar;

    // UI State
    private boolean showDemoWindow = false;
    private boolean showAboutWindow = false;
    private String statusMessage = "Ready";

    public View(Model model) {
        this.model = model;
        this.model.addObserver(this);

        // Initialize UI panels
        this.menuBar = new MenuBar();
        this.projectPanel = new ProjectPanel();
        this.assetExplorer = new AssetExplorer();
        this.propertyPanel = new PropertyPanel();
        this.viewport3D = new Viewport3D();
        this.statusBar = new StatusBar();
    }

    public void render() {
        setupDockspace();

        // Render main panels
        renderProjectPanel();
        renderAssetExplorer();
        renderPropertyPanel();
        renderViewport3D();

        // Optional windows
        if (showDemoWindow) {
            ImGui.showDemoWindow();
        }

        if (showAboutWindow) {
            renderAboutWindow();
        }
    }

    private void setupDockspace() {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse;
        windowFlags |= ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove;
        windowFlags |= ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.setNextWindowPos(0, 0);
        ImGui.setNextWindowSize(ImGui.getIO().getDisplaySizeX(), ImGui.getIO().getDisplaySizeY());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);

        ImGui.begin("DockSpace", windowFlags);
        ImGui.popStyleVar(2);

        // Render menu bar within the dockspace
        menuBar.render(this);

        // DockSpace
        int dockspaceId = ImGui.getID("MainDockSpace");
        ImGui.dockSpace(dockspaceId);

        // Render status bar at the bottom of the dockspace window
        ImGui.separator();
        ImGui.text(statusMessage);

        ImGui.end();
    }

    private void renderProjectPanel() {
        ImGui.begin("Project");
        projectPanel.render(model);
        ImGui.end();
    }

    private void renderAssetExplorer() {
        ImGui.begin("Asset Explorer");
        assetExplorer.render(model);
        ImGui.end();
    }

    private void renderPropertyPanel() {
        ImGui.begin("Properties");
        propertyPanel.render(model);
        ImGui.end();
    }

    private void renderViewport3D() {
        ImGui.begin("3D Viewport");
        viewport3D.render(model);
        ImGui.end();
    }

    private void renderAboutWindow() {
        if (ImGui.begin("About ModularGuns Studio")) {
            ImGui.text("ModularGuns Studio v1.0");
            ImGui.text("A lightweight gun pack editor for modular weapon systems");
            ImGui.separator();
            ImGui.text("Built with:");
            ImGui.bulletText("LWJGL 3.3.3");
            ImGui.bulletText("Dear ImGui");
            ImGui.bulletText("OpenGL");

            if (ImGui.button("Close")) {
                showAboutWindow = false;
            }
        }
        ImGui.end();
    }

    // Event handlers called by UI components
    public void onNewProject() {
        // This will be handled by controller
    }

    public void onLoadProject() {
        // This will be handled by controller
    }

    public void onSaveProject() {
        // This will be handled by controller
    }

    public void onExportPack() {
        // This will be handled by controller
    }

    public void showDemoWindow(boolean show) {
        this.showDemoWindow = show;
    }

    public void showAboutWindow(boolean show) {
        this.showAboutWindow = show;
    }

    @Override
    public void onModelChanged(ModelEvent event) {
        switch (event.getType()) {
            case PROJECT_LOADED:
                statusMessage = "Project loaded: " + model.getCurrentProject().getName();
                break;
            case PROJECT_SAVED:
                statusMessage = "Project saved successfully";
                break;
            case PROJECT_SAVE_FAILED:
                statusMessage = "Failed to save project: " + event.getData();
                break;
            case PACK_CREATED:
                statusMessage = "Gun pack created: " + ((GunPack)event.getData()).getName();
                break;
            case ASSET_IMPORTED:
                statusMessage = "Asset imported: " + ((Asset)event.getData()).getName();
                break;
            case ASSET_IMPORT_FAILED:
                statusMessage = "Asset import failed: " + event.getData();
                break;
            default:
                break;
        }
    }

    public void cleanup() {
        model.removeObserver(this);
        viewport3D.cleanup();
    }
}
