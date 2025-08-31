package net.modularmods.service;

import lombok.Getter;
import lombok.Setter;
import net.modularmods.model.Project;
import net.modularmods.model.ModelEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Service for managing project operations
 */
@Getter
@Setter
public class ProjectService {
    private Project currentProject;
    private final Gson gson;
    private Path projectDirectory;

    public ProjectService() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public Project createNewProject(String name, String directory) {
        currentProject = new Project(name);
        projectDirectory = Paths.get(directory, name);

        // Create project directory structure
        createProjectStructure();

        currentProject.notifyObservers(new ModelEvent(ModelEvent.Type.PROJECT_CREATED, currentProject));
        return currentProject;
    }

    public Project loadProject(String projectPath) throws IOException {
        Path projectFile = Paths.get(projectPath, "project.json");

        try (FileReader reader = new FileReader(projectFile.toFile())) {
            currentProject = gson.fromJson(reader, Project.class);
            projectDirectory = Paths.get(projectPath);

            currentProject.notifyObservers(new ModelEvent(ModelEvent.Type.PROJECT_LOADED, currentProject));
            return currentProject;
        }
    }

    public void saveProject() throws IOException {
        if (currentProject == null || projectDirectory == null) {
            throw new IllegalStateException("No project to save");
        }

        Path projectFile = projectDirectory.resolve("project.json");

        try (FileWriter writer = new FileWriter(projectFile.toFile())) {
            gson.toJson(currentProject, writer);
            currentProject.setModified(false);

            currentProject.notifyObservers(new ModelEvent(ModelEvent.Type.PROJECT_SAVED, currentProject));
        }
    }

    public void saveProjectAs(String directory) throws IOException {
        if (currentProject == null) {
            throw new IllegalStateException("No project to save");
        }

        projectDirectory = Paths.get(directory, currentProject.getName());
        createProjectStructure();
        saveProject();
    }

    private void createProjectStructure() {
        if (projectDirectory != null) {
            projectDirectory.toFile().mkdirs();
            projectDirectory.resolve("assets").toFile().mkdirs();
            projectDirectory.resolve("assets/textures").toFile().mkdirs();
            projectDirectory.resolve("assets/models").toFile().mkdirs();
            projectDirectory.resolve("assets/sounds").toFile().mkdirs();
            projectDirectory.resolve("gunpacks").toFile().mkdirs();
        }
    }

    public boolean hasUnsavedChanges() {
        return currentProject != null && currentProject.isModified();
    }
}
