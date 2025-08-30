package net.modularmods;

import net.modularmods.core.Application;

public class Main {
    public static void main(String[] args) {
        try {
            Application app = new Application();
            app.run();
        } catch (Exception e) {
            System.err.println("Application failed to start: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
}