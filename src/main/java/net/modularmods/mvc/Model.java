package net.modularmods.mvc;

import lombok.Getter;
import lombok.Setter;
import net.modularmods.model.ModelEvent;
import net.modularmods.model.ModelObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Base Model class for the MVC pattern
 */
@Getter
@Setter
public abstract class Model {
    private List<ModelObserver> observers = new ArrayList<>();

    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(ModelEvent event) {
        observers.forEach(observer -> observer.onModelChanged(event));
    }
}
