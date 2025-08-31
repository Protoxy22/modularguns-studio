package net.modularmods.mvc;

import lombok.Getter;
import lombok.Setter;

/**
 * Base Controller class for the MVC pattern
 */
@Getter
@Setter
public abstract class Controller {
    protected Model model;
    protected View view;

    public Controller(Model model) {
        this.model = model;
    }

    public void setView(View view) {
        this.view = view;
        view.setController(this);
    }

    public abstract void init();
    public abstract void cleanup();
}
