/*
 *     Copyright (C) 2016 boomboompower
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.boomboompower.nicknames.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.boomboompower.nicknames.NicknamesMain;
import me.boomboompower.nicknames.gui.CapeGui;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtils {

    private File configFile;

    private JsonObject config = new JsonObject();

    public FileUtils(File configFile) {
        this.configFile = configFile;
    }

    public boolean configExists() {
        return exists(configFile.getPath());
    }

    public void loadConfig() {
        if (configExists()) {
            log("Config file exists! Reading...");
            try {
                FileReader reader = new FileReader(configFile);
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder builder = new StringBuilder();

                String currentLine;
                while ((currentLine = bufferedReader.readLine()) != null) {
                    builder.append(currentLine);
                }
                String complete = builder.toString();

                config = new JsonParser().parse(complete).getAsJsonObject();
            } catch (Exception ex) {
                System.out.println("Could not write config! Saving...");
                saveConfig();
            }
            log(NicknamesMain.nickname = config.has("nickname") ? config.get("nickname").getAsString() : null);
            log(NicknamesMain.displayedCapeType = config.has("capetype") ? CapeGui.CapeType.valueOf(config.get("capetype").getAsString()) : CapeGui.CapeType.NONE);
            log(NicknamesMain.skinName = config.has("skinname") ? !config.get("skinname").getAsString().isEmpty() ? config.get("skinname").getAsString() : null : null);
            log(NicknamesMain.useRanks = config.has("useranks") && config.get("useranks").getAsBoolean());
        } else {
            log("Config does not exist! Saving...");
            saveConfig();
        }
    }

    public void saveConfig() {
        config = new JsonObject();
        try {
            configFile.createNewFile();
            FileWriter writer = new FileWriter(configFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            config.addProperty("nickname", NicknamesMain.nickname != null ? NicknamesMain.nickname : NicknamesMain.userName);
            config.addProperty("capetype", (NicknamesMain.displayedCapeType != CapeGui.CapeType.CUSTOM) ? NicknamesMain.displayedCapeType.name() : CapeGui.CapeType.NONE.name());
            config.addProperty("skinname", NicknamesMain.skinName != null ? NicknamesMain.skinName : NicknamesMain.userName);
            config.addProperty("useranks", NicknamesMain.useRanks);

            bufferedWriter.write(config.toString());
            bufferedWriter.close();
            writer.close();
        } catch (Exception ex) {
            log("Could not save config!");
            ex.printStackTrace();
        }
    }

    private boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }

    private void log(Object message, String... replacements) {
        Logger.getLogger("FileUtils").log(Level.INFO, String.format(String.valueOf(message), (Object) replacements));
    }
}
