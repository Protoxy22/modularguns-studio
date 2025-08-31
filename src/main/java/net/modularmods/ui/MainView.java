package net.modularmods.ui;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import lombok.Getter;
import lombok.Setter;
import net.modularmods.core.Window;
import net.modularmods.model.ModelEvent;
import net.modularmods.mvc.View;

/**
 * Main View implementation for the MVC pattern - handles all UI rendering
 */
@Getter
@Setter
public class MainView extends View {
    private Window window;
    private ImGuiImplGlfw imGuiGlfw;
    private ImGuiImplGl3 imGuiGl3;

    // UI Components
    private MenuBar menuBar;
    private AssetExplorer assetExplorer;
    private PropertyPanel propertyPanel;
    private Viewport3D viewport3D;
    private ContentPackPanel contentPackPanel;
    private TemplatesPanel templatesPanel;

    // Flag for first run layout setup
    private boolean firstRun = true;

    public MainView(Window window) {
        this.window = window;
    }

    @Override
    public void init() {
        initImGui();
    }

    private void initImGui() {
        // Initialize ImGui context
        ImGui.createContext();

        // Initialize ImGuiIO
        final ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        // Remove ViewportsEnable to disable rendering outside of window
        // io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

        // Initialize ImGui GLFW implementation
        imGuiGlfw = new ImGuiImplGlfw();
        imGuiGlfw.init(window.getWindowHandle(), true);

        // Initialize ImGui OpenGL3 implementation
        imGuiGl3 = new ImGuiImplGl3();
        imGuiGl3.init("#version 150");
    }

    public void setUIComponents(MenuBar menuBar, AssetExplorer assetExplorer,
                               PropertyPanel propertyPanel, Viewport3D viewport3D,
                               ContentPackPanel contentPackPanel, TemplatesPanel templatesPanel) {
        this.menuBar = menuBar;
        this.assetExplorer = assetExplorer;
        this.propertyPanel = propertyPanel;
        this.viewport3D = viewport3D;
        this.contentPackPanel = contentPackPanel;
        this.templatesPanel = templatesPanel;
    }

    @Override
    public void render() {
        // Start ImGui frame
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        // Create main dockspace with professional layout
        setupDockingLayout();

        // Render UI components
        if (menuBar != null) menuBar.render();
        if (contentPackPanel != null) contentPackPanel.render();
        if (templatesPanel != null) templatesPanel.render();
        if (assetExplorer != null) assetExplorer.render();
        if (propertyPanel != null) propertyPanel.render();
        if (viewport3D != null) viewport3D.render();

        // Remove demo window for professional appearance
        // ImGui.showDemoWindow();

        // Render status bar
        renderStatusBar();

        // Render ImGui
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    private void setupDockingLayout() {
        // Get main viewport and create dockspace
        int mainDockspaceId = ImGui.dockSpaceOverViewport(ImGui.getMainViewport());

        // For now, we'll use a simple dockspace without programmatic layout
        // The user can manually arrange the windows on first run
        // This ensures compatibility with the available ImGui Java binding

        // The windows will automatically dock to the main dockspace when dragged
        // Users can arrange them according to the desired layout:
        // - Content Packs: top-left
        // - Templates: below Content Packs
        // - Viewport: center/right
        // - Asset Explorer: bottom full width
    }

    private void renderStatusBar() {
        if (ImGui.begin("Status Bar")) {
            ImGui.text(String.format("Ready | FPS: %.1f | ModularGuns Studio", ImGui.getIO().getFramerate()));
        }
        ImGui.end();
    }

    @Override
    public void cleanup() {
        // Cleanup ImGui
        if (imGuiGl3 != null) imGuiGl3.dispose();
        if (imGuiGlfw != null) imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    @Override
    protected void update(ModelEvent event) {
        // Handle model changes that affect the view
        // UI components will handle their own updates through their observer implementations
    }
}
