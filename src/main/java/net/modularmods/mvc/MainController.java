package net.modularmods.mvc;

import lombok.Getter;
import net.modularmods.core.Window;
import net.modularmods.model.Project;
import net.modularmods.service.AssetService;
import net.modularmods.service.ProjectService;
import net.modularmods.service.RenderService;
import net.modularmods.ui.*;

/**
 * Main Controller class - coordinates between Model and View
 */
@Getter
public class MainController extends Controller {
    private final Window window;
    private final ProjectService projectService;
    private final AssetService assetService;
    private final RenderService renderService;

    // UI Components
    private MenuBar menuBar;
    private AssetExplorer assetExplorer;
    private PropertyPanel propertyPanel;
    private Viewport3D viewport3D;
    private ContentPackPanel contentPackPanel;
    private TemplatesPanel templatesPanel;

    public MainController(Window window) {
        super(new Project("Untitled")); // Default project as model
        this.window = window;

        // Initialize services
        this.projectService = new ProjectService();
        this.assetService = new AssetService(projectService);
        this.renderService = new RenderService(window);
    }

    @Override
    public void init() {
        // Initialize render service
        renderService.init();

        // Create UI components
        menuBar = new MenuBar(projectService);
        assetExplorer = new AssetExplorer(assetService, projectService);
        propertyPanel = new PropertyPanel(assetService);
        viewport3D = new Viewport3D(renderService);
        contentPackPanel = new ContentPackPanel(projectService); // Initialize ContentPackPanel with projectService
        templatesPanel = new TemplatesPanel(projectService); // Initialize TemplatesPanel with projectService

        // Set up model observers
        if (model instanceof Project) {
            Project project = (Project) model;
            project.addObserver(assetExplorer);
            project.addObserver(propertyPanel);
            project.addObserver(contentPackPanel);
            project.addObserver(templatesPanel);
        }

        // Initialize view if it's set
        if (view != null) {
            view.init();
            if (view instanceof MainView) {
                ((MainView) view).setUIComponents(menuBar, assetExplorer, propertyPanel, viewport3D, contentPackPanel, templatesPanel);
            }
        }
    }

    @Override
    public void cleanup() {
        // Cleanup services and UI components
        if (view != null) {
            view.cleanup();
        }
    }

    public void update() {
        // Handle any per-frame updates
        // This could include input handling, animation updates, etc.
    }

    // Convenience methods for common operations
    public void createNewProject(String name, String directory) {
        try {
            Project newProject = projectService.createNewProject(name, directory);
            setModel(newProject);
            setupModelObservers(newProject);
        } catch (Exception e) {
            System.err.println("Failed to create new project: " + e.getMessage());
        }
    }

    public void loadProject(String projectPath) {
        try {
            Project loadedProject = projectService.loadProject(projectPath);
            setModel(loadedProject);
            setupModelObservers(loadedProject);
        } catch (Exception e) {
            System.err.println("Failed to load project: " + e.getMessage());
        }
    }

    public void saveCurrentProject() {
        try {
            projectService.saveProject();
        } catch (Exception e) {
            System.err.println("Failed to save project: " + e.getMessage());
        }
    }

    private void setupModelObservers(Project project) {
        project.addObserver(assetExplorer);
        project.addObserver(propertyPanel);
    }

    private void setModel(Project project) {
        this.model = project;
        // Update the current project in the service
        // Note: This is a simplification - in a real implementation,
        // you might want to handle this differently
    }
}
