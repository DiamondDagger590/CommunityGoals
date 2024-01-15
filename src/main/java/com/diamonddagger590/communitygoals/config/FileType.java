package com.diamonddagger590.communitygoals.config;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.diamonddagger590.communitygoals.CommunityGoals;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public enum FileType {

    GOALS_CONFIG("goals.yml");

    private static final String FOLDER_PATH = CommunityGoals.getInstance().getDataFolder().getPath();

    private final String filePath;
    private final Optional<Class<? extends SettingsHolder>> clazz;

    FileType(@NotNull String filePath) {
        this.filePath = filePath;
        this.clazz = Optional.empty();
    }

    FileType(@NotNull String filePath, @NotNull Class<? extends SettingsHolder> clazz) {
        this.filePath = filePath;
        this.clazz = Optional.of(clazz);
    }

    public boolean isConfigMeFile() {
        return clazz.isPresent();
    }

    @NotNull
    public CustomConfiguration initializeCustomConfigFile() {
        return new CustomConfiguration(filePath);
    }

    @NotNull
    public SettingsManager initializeConfigMeFile() {
        if (clazz.isEmpty()) {
            throw new IllegalArgumentException("Tried to load a configme file when it isn't");
        }
        File configFile = new File(FOLDER_PATH + File.separator + filePath);
        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return SettingsManagerBuilder
                .withYamlFile(configFile)
                .configurationData(clazz.get())
                .useDefaultMigrationService()
                .create();
    }

}
