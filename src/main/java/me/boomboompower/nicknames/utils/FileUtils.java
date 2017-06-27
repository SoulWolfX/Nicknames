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
import me.boomboompower.nicknames.gui.CapeSelectionGUI;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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
            System.out.println("Config file exists! Reading...");
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
            System.out.println(NicknamesMain.nickname = config.has("nickname") ? config.get("nickname").getAsString() : null);
            System.out.println(NicknamesMain.displayedCapeType = config.has("capetype") ? CapeSelectionGUI.CapeType.valueOf(config.get("capetype").getAsString()) : CapeSelectionGUI.CapeType.NONE);
            System.out.println(NicknamesMain.skinName = config.has("skinname") ? !config.get("skinname").getAsString().isEmpty() ? config.get("skinname").getAsString() : null : null);
            System.out.println(NicknamesMain.useRanks = config.has("useranks") && config.get("useranks").getAsBoolean());
        } else {
            System.out.println("Config does not exist! Saving...");
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
            config.addProperty("capetype", NicknamesMain.displayedCapeType.name());
            config.addProperty("skinname", NicknamesMain.skinName != null ? NicknamesMain.skinName : NicknamesMain.userName);
            config.addProperty("useranks", NicknamesMain.useRanks);

            bufferedWriter.write(config.toString());
            bufferedWriter.close();
            writer.close();
        } catch (Exception ex) {
            System.out.println("Could not save config!");
            ex.printStackTrace();
        }
    }

    private boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }
}
