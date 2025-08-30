package net.modularmods.mvc;

import net.modularmods.model.*;
import net.modularmods.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Model {
    // Project and Pack Management
    private Project currentProject;
    private final List<GunPack> gunPacks;

    // Current Selection
    private GunPack selectedPack;
    private Gun selectedGun;
    private Asset selectedAsset;

    // Services
    private final ProjectService projectService;
    private final AssetService assetService;
    private final ExportService exportService;
    private final RenderService renderService;

    // Observers for MVC communication
    private final List<ModelObserver> observers;

    public Model() {
        this.gunPacks = new CopyOnWriteArrayList<>();
        this.observers = new CopyOnWriteArrayList<>();

        // Initialize services
        this.projectService = new ProjectService();
        this.assetService = new AssetService();
        this.exportService = new ExportService();
        this.renderService = new RenderService();

        // Initialize with empty project
        this.currentProject = new Project("Untitled Project", "");
    }

    // Observer pattern for MVC communication
    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(ModelEvent event) {
        for (ModelObserver observer : observers) {
            observer.onModelChanged(event);
        }
    }

    // Project Management
    public Project getCurrentProject() { return currentProject; }

    public void createNewProject(String name, String path) {
        currentProject = projectService.createProject(name, path);
        gunPacks.clear();
        selectedPack = null;
        selectedGun = null;
        selectedAsset = null;
        notifyObservers(new ModelEvent(ModelEvent.Type.PROJECT_CHANGED, currentProject));
    }

    public void loadProject(String path) {
        try {
            currentProject = projectService.loadProject(path);
            gunPacks.clear();
            gunPacks.addAll(currentProject.getGunPacks());
            notifyObservers(new ModelEvent(ModelEvent.Type.PROJECT_LOADED, currentProject));
        } catch (Exception e) {
            notifyObservers(new ModelEvent(ModelEvent.Type.PROJECT_LOAD_FAILED, e.getMessage()));
        }
    }

    public void saveProject() {
        try {
            projectService.saveProject(currentProject);
            notifyObservers(new ModelEvent(ModelEvent.Type.PROJECT_SAVED, currentProject));
        } catch (Exception e) {
            notifyObservers(new ModelEvent(ModelEvent.Type.PROJECT_SAVE_FAILED, e.getMessage()));
        }
    }

    // Gun Pack Management
    public List<GunPack> getGunPacks() { return new ArrayList<>(gunPacks); }

    public GunPack getSelectedPack() { return selectedPack; }

    public void createGunPack(String name, String description, String version) {
        GunPack pack = new GunPack(name, description, version);
        gunPacks.add(pack);
        currentProject.addGunPack(pack);
        setSelectedPack(pack);
        notifyObservers(new ModelEvent(ModelEvent.Type.PACK_CREATED, pack));
    }

    public void setSelectedPack(GunPack pack) {
        selectedPack = pack;
        selectedGun = null; // Clear gun selection when pack changes
        notifyObservers(new ModelEvent(ModelEvent.Type.PACK_SELECTED, pack));
    }

    public void deleteGunPack(GunPack pack) {
        gunPacks.remove(pack);
        currentProject.removeGunPack(pack);
        if (selectedPack == pack) {
            selectedPack = null;
            selectedGun = null;
        }
        notifyObservers(new ModelEvent(ModelEvent.Type.PACK_DELETED, pack));
    }

    // Gun Management
    public Gun getSelectedGun() { return selectedGun; }

    public void createGun(String name) {
        if (selectedPack == null) return;

        Gun gun = new Gun(name);
        selectedPack.addGun(gun);
        setSelectedGun(gun);
        notifyObservers(new ModelEvent(ModelEvent.Type.GUN_CREATED, gun));
    }

    public void setSelectedGun(Gun gun) {
        selectedGun = gun;
        notifyObservers(new ModelEvent(ModelEvent.Type.GUN_SELECTED, gun));
    }

    public void deleteGun(Gun gun) {
        if (selectedPack != null) {
            selectedPack.removeGun(gun);
            if (selectedGun == gun) {
                selectedGun = null;
            }
            notifyObservers(new ModelEvent(ModelEvent.Type.GUN_DELETED, gun));
        }
    }

    // Asset Management
    public Asset getSelectedAsset() { return selectedAsset; }

    public void setSelectedAsset(Asset asset) {
        selectedAsset = asset;
        notifyObservers(new ModelEvent(ModelEvent.Type.ASSET_SELECTED, asset));
    }

    public void importAsset(String filePath, Asset.Type type) {
        try {
            Asset asset = assetService.importAsset(filePath, type);
            if (selectedPack != null) {
                selectedPack.addAsset(asset);
                notifyObservers(new ModelEvent(ModelEvent.Type.ASSET_IMPORTED, asset));
            }
        } catch (Exception e) {
            notifyObservers(new ModelEvent(ModelEvent.Type.ASSET_IMPORT_FAILED, e.getMessage()));
        }
    }

    // Service Access
    public ProjectService getProjectService() { return projectService; }
    public AssetService getAssetService() { return assetService; }
    public ExportService getExportService() { return exportService; }
    public RenderService getRenderService() { return renderService; }

    public void cleanup() {
        renderService.cleanup();
        observers.clear();
    }
}
