package net.modularmods.core;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.modularmods.mvc.Controller;
import net.modularmods.mvc.Model;
import net.modularmods.mvc.View;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Application {
    private long window;
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    // MVC Components
    private Model model;
    private View view;
    private Controller controller;

    private static final int WINDOW_WIDTH = 1600;
    private static final int WINDOW_HEIGHT = 900;
    private static final String WINDOW_TITLE = "ModularGuns Studio";

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        // Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        // Create window
        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Center window
        var vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode != null) {
            glfwSetWindowPos(window,
                (vidMode.width() - WINDOW_WIDTH) / 2,
                (vidMode.height() - WINDOW_HEIGHT) / 2);
        }

        // Make OpenGL context current
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // Enable v-sync
        glfwShowWindow(window);

        // Initialize OpenGL
        GL.createCapabilities();

        // Initialize ImGui
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

        imGuiGlfw.init(window, true);
        imGuiGl3.init("#version 330 core");

        // Initialize MVC components
        initializeMVC();

        System.out.println("ModularGuns Studio initialized successfully");
    }

    private void initializeMVC() {
        model = new Model();
        view = new View(model);
        controller = new Controller(model, view);
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            // Start ImGui frame
            imGuiGlfw.newFrame();
            ImGui.newFrame();

            // Update controller (handles input and model updates)
            controller.update();

            // Render view
            view.render();

            // Render ImGui
            ImGui.render();

            // Clear framebuffer
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Render ImGui draw data
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            // Handle multi-viewport
            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupCurrentContext = glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                glfwMakeContextCurrent(backupCurrentContext);
            }

            glfwSwapBuffers(window);
        }
    }

    private void cleanup() {
        // Cleanup MVC
        if (controller != null) controller.cleanup();
        if (view != null) view.cleanup();
        if (model != null) model.cleanup();

        // Cleanup ImGui
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();

        // Cleanup GLFW
        glfwDestroyWindow(window);
        glfwTerminate();

        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null) {
            callback.free();
        }

        System.out.println("Application shutdown complete");
    }
}
