package net.modularmods.mvc;

import net.modularmods.model.ModelEvent;
import net.modularmods.model.ModelObserver;

/**
 * Base View abstract class for the MVC pattern
 */
public abstract class View implements ModelObserver {
    protected Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public abstract void init();
    public abstract void render();
    public abstract void cleanup();

    @Override
    public void onModelChanged(ModelEvent event) {
        // Default implementation - can be overridden by subclasses
        update(event);
    }

    protected abstract void update(ModelEvent event);
}
