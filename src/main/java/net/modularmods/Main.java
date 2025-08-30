package net.modularmods;

import net.modularmods.editor.GunStudioApplication;

public class Main {
    public static void main(String[] args) {
        try {
            GunStudioApplication app = new GunStudioApplication();
            app.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}