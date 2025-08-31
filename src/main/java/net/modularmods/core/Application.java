package net.modularmods.core;

import lombok.Getter;
import net.modularmods.mvc.MainController;
import net.modularmods.ui.MainView;

/**
 * Main Application class - simplified to use MVC architecture
 */
@Getter
public class Application {
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 720;
    private static final String WINDOW_TITLE = "ModularGuns Studio";

    private Window window;
    private MainController controller;
    private MainView view;
    private boolean running = true;

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        // Initialize window
        window = new Window(WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, true);
        window.init();

        // Initialize MVC components
        controller = new MainController(window);
        view = new MainView(window);

        // Wire up MVC
        controller.setView(view);
        controller.init();

        System.out.println("ModularGuns Studio initialized successfully!");
    }

    private void loop() {
        while (running && !window.shouldClose()) {
            // Clear the framebuffer
            window.clear();

            // Update controller
            controller.update();

            // Render view
            view.render();

            // Swap buffers and poll events
            window.update();
        }
    }

    private void cleanup() {
        // Cleanup MVC components
        if (controller != null) {
            controller.cleanup();
        }

        // Cleanup window
        if (window != null) {
            window.cleanup();
        }

        System.out.println("Application cleaned up successfully!");
    }

    public void stop() {
        running = false;
    }
}
