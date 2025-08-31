package net.modularmods.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Event class for model changes
 */
@Getter
@AllArgsConstructor
public class ModelEvent {
    public enum Type {
        PROJECT_CREATED,
        PROJECT_LOADED,
        PROJECT_SAVED,
        ASSET_ADDED,
        ASSET_REMOVED,
        ASSET_SELECTED,
        PROPERTY_CHANGED
    }

    private final Type type;
    private final Object data;
    private final String message;

    public ModelEvent(Type type, Object data) {
        this.type = type;
        this.data = data;
        this.message = null;
    }

    public ModelEvent(Type type) {
        this.type = type;
        this.data = null;
        this.message = null;
    }
}
