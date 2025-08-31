package net.modularmods.model;

/**
 * Observer interface for model changes
 */
public interface ModelObserver {
    void onModelChanged(ModelEvent event);
}
