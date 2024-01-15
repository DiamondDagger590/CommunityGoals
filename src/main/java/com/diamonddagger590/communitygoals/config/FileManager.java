package com.diamonddagger590.communitygoals.config;

import ch.jalu.configme.SettingsManager;
import com.diamonddagger590.communitygoals.CommunityGoals;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FileManager {

    private final CommunityGoals communityGoals;
    private final Map<FileType, CustomConfiguration> loadedCustomFiles;
    private final Map<FileType, SettingsManager> loadedConfigMeFiles;

    public FileManager(@NotNull CommunityGoals communityGoals) {
        this.communityGoals = communityGoals;
        this.loadedCustomFiles = new HashMap<>();
        this.loadedConfigMeFiles = new HashMap<>();

        if (!communityGoals.getDataFolder().exists()) {
            communityGoals.getDataFolder().mkdirs();
        }

        loadFiles();
    }

    private void loadFiles() {

        for(FileType fileType : FileType.values()) {
            if (fileType.isConfigMeFile()) {
                loadedConfigMeFiles.put(fileType, fileType.initializeConfigMeFile());
            } else {
                loadedCustomFiles.put(fileType, fileType.initializeCustomConfigFile());
            }
        }
    }

    public void reloadFiles() {
        for (SettingsManager settingsManager : loadedConfigMeFiles.values()) {
            settingsManager.reload();
        }
        for (CustomConfiguration customConfiguration : loadedCustomFiles.values()) {
            customConfiguration.reloadFile();
        }
    }

    @NotNull
    public SettingsManager getConfigMeFile(@NotNull FileType fileType) {
        return loadedConfigMeFiles.get(fileType);
    }

    @NotNull
    public CustomConfiguration getCustomFile(@NotNull FileType fileType) {
        return loadedCustomFiles.get(fileType);
    }
}
