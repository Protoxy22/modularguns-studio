package net.modularmods.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.modularmods.model.Project;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProjectService {
    private final Gson gson;
    private static final String PROJECT_FILE_NAME = "project.json";

    public ProjectService() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public Project createProject(String name, String path) {
        Project project = new Project(name, path);

        // Create project directory if it doesn't exist
        try {
            Path projectPath = Paths.get(path, name);
            Files.createDirectories(projectPath);
            project.setPath(projectPath.toString());

            // Create standard project folders
            Files.createDirectories(projectPath.resolve("assets"));
            Files.createDirectories(projectPath.resolve("models"));
            Files.createDirectories(projectPath.resolve("textures"));
            Files.createDirectories(projectPath.resolve("animations"));
            Files.createDirectories(projectPath.resolve("exports"));

        } catch (IOException e) {
            System.err.println("Failed to create project directories: " + e.getMessage());
        }

        return project;
    }

    public Project loadProject(String projectPath) throws IOException {
        File projectFile = new File(projectPath, PROJECT_FILE_NAME);
        if (!projectFile.exists()) {
            throw new IOException("Project file not found: " + projectFile.getAbsolutePath());
        }

        try (FileReader reader = new FileReader(projectFile)) {
            Project project = gson.fromJson(reader, Project.class);
            project.setPath(projectPath);
            return project;
        }
    }

    public void saveProject(Project project) throws IOException {
        File projectDir = new File(project.getPath());
        if (!projectDir.exists()) {
            projectDir.mkdirs();
        }

        File projectFile = new File(projectDir, PROJECT_FILE_NAME);
        try (FileWriter writer = new FileWriter(projectFile)) {
            gson.toJson(project, writer);
        }
    }

    public boolean isValidProjectPath(String path) {
        File projectFile = new File(path, PROJECT_FILE_NAME);
        return projectFile.exists() && projectFile.isFile();
    }
}
