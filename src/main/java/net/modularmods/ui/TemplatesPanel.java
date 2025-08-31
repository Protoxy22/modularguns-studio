package net.modularmods.ui;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import lombok.Getter;
import lombok.Setter;
import net.modularmods.model.ModelEvent;
import net.modularmods.model.ModelObserver;
import net.modularmods.service.ProjectService;

/**
 * Templates panel for managing gun templates and presets
 */
@Getter
@Setter
public class TemplatesPanel implements ModelObserver {
    private final ProjectService projectService;

    // Template categories
    private final String[] templateCategories = {
        "Assault Rifles", "Pistols", "Shotguns", "Sniper Rifles",
        "SMGs", "LMGs", "Explosives", "Melee", "Custom"
    };

    public TemplatesPanel(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void render() {
        if (ImGui.begin("Templates")) {
            renderToolbar();
            ImGui.separator();
            renderTemplateCategories();
        }
        ImGui.end();
    }

    private void renderToolbar() {
        if (ImGui.button("New Template")) {
            // TODO: Create new template from current gun
        }

        ImGui.sameLine();
        if (ImGui.button("Import")) {
            // TODO: Import template from file
        }

        ImGui.sameLine();
        if (ImGui.button("Refresh")) {
            // TODO: Refresh template list
        }
    }

    private void renderTemplateCategories() {
        if (projectService.getCurrentProject() == null) {
            ImGui.textColored(0.7f, 0.7f, 0.7f, 1.0f, "No project loaded");
            return;
        }

        for (String category : templateCategories) {
            renderTemplateCategory(category);
        }
    }

    private void renderTemplateCategory(String categoryName) {
        int flags = ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.OpenOnDoubleClick;

        if (ImGui.treeNodeEx(categoryName, flags)) {
            ImGui.indent();

            // For now, show placeholder templates
            String[] templates = getTemplatesForCategory(categoryName);

            if (templates.length == 0) {
                ImGui.textColored(0.6f, 0.6f, 0.6f, 1.0f, "No templates");
            } else {
                for (String template : templates) {
                    if (ImGui.selectable(template)) {
                        // TODO: Apply template to current gun
                    }

                    if (ImGui.beginPopupContextItem()) {
                        if (ImGui.menuItem("Apply to Current")) {
                            // TODO: Apply template to current gun
                        }
                        if (ImGui.menuItem("Create Gun from Template")) {
                            // TODO: Create new gun from template
                        }
                        ImGui.separator();
                        if (ImGui.menuItem("Edit Template")) {
                            // TODO: Edit template
                        }
                        if (ImGui.menuItem("Delete Template")) {
                            // TODO: Delete template
                        }
                        ImGui.endPopup();
                    }
                }
            }

            ImGui.unindent();
            ImGui.treePop();
        }
    }

    private String[] getTemplatesForCategory(String category) {
        // Placeholder templates - in a real implementation,
        // these would come from the project or template service
        switch (category) {
            case "Assault Rifles":
                return new String[]{"AK-47 Template", "M4A1 Template", "SCAR Template"};
            case "Pistols":
                return new String[]{"Glock Template", "1911 Template", "Desert Eagle Template"};
            case "Shotguns":
                return new String[]{"Pump Action Template", "Auto Shotgun Template"};
            case "Sniper Rifles":
                return new String[]{"Bolt Action Template", "Semi-Auto Template"};
            case "SMGs":
                return new String[]{"MP5 Template", "UZI Template"};
            case "LMGs":
                return new String[]{"M249 Template", "PKM Template"};
            case "Explosives":
                return new String[]{"Grenade Launcher Template", "Rocket Launcher Template"};
            case "Melee":
                return new String[]{"Knife Template", "Sword Template"};
            case "Custom":
                return new String[]{}; // User-created templates
            default:
                return new String[]{};
        }
    }

    @Override
    public void onModelChanged(ModelEvent event) {
        // Handle model changes if needed
        // Could refresh template list based on project changes
    }
}
