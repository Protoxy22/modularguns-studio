package net.modularmods.model;

public class ModelEvent {
    public enum Type {
        PROJECT_CHANGED,
        PROJECT_LOADED,
        PROJECT_SAVED,
        PROJECT_LOAD_FAILED,
        PROJECT_SAVE_FAILED,
        PACK_CREATED,
        PACK_SELECTED,
        PACK_DELETED,
        GUN_CREATED,
        GUN_SELECTED,
        GUN_DELETED,
        ASSET_IMPORTED,
        ASSET_SELECTED,
        ASSET_IMPORT_FAILED
    }

    private final Type type;
    private final Object data;

    public ModelEvent(Type type, Object data) {
        this.type = type;
        this.data = data;
    }

    public Type getType() { return type; }
    public Object getData() { return data; }
}
