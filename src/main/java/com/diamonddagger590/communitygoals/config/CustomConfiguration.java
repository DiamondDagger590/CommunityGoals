package com.diamonddagger590.communitygoals.config;

import com.diamonddagger590.communitygoals.CommunityGoals;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class CustomConfiguration {

    private File file;
    private FileConfiguration fileConfiguration;
    private String filePath;

    public CustomConfiguration(@NotNull String filePath) {
        this.filePath = filePath;
        initializeFile();
    }

    public void initializeFile() {
        saveResource(filePath, false);
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Overrides the loaded state file and loads the file systems file.
     */
    public void reloadFile() {
        file = new File(CommunityGoals.getInstance().getDataFolder(), filePath);
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    /**
     * Was found here: https://bukkit.org/threads/extracting-file-from-jar.16962
     */
    private void copyFile(InputStream in, File out) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(out)) {
            byte[] buf = new byte[1024];
            int i;
            while ((i = in.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } finally {
            if (in != null) {
                in.close();
            }

        }
    }

    private void saveResource(String resourcePath, boolean replace) {
        CommunityGoals communityGoals = CommunityGoals.getInstance();
        if (resourcePath != null && !resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = communityGoals.getResource(resourcePath);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource \'" + resourcePath + "\' cannot be found");
            } else {
                File outFile = new File(communityGoals.getDataFolder(), resourcePath);
                file = outFile;
                int lastIndex = resourcePath.lastIndexOf(47);
                File outDir = new File(communityGoals.getDataFolder(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }
                try {
                    if (outFile.exists() && !replace) {
//                        communityGoals.getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
                    } else {
                        FileOutputStream ex = new FileOutputStream(outFile);
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            ex.write(buf, 0, len);
                        }
                        ex.close();
                        in.close();
                    }
                } catch (IOException var10) {
                    communityGoals.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, var10);
                }

            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }
}
