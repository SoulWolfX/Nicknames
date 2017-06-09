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

import me.boomboompower.nicknames.NicknamesMain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

   public FileUtils() {
   }

   public static void getVars() throws Throwable {
       try {
           File e = new File(NicknamesMain.USER_DIR);
           if (!e.exists()) {
               e.mkdirs();
           }

           boolean executeWriter = false;
           boolean options = false;
           boolean settings = false;

           if (exists(NicknamesMain.USER_DIR + "options.nn")) {
               BufferedReader f = new BufferedReader(new FileReader(NicknamesMain.USER_DIR + "options.nn"));

               List lines = f.lines().collect(Collectors.toList());

               if (lines.size() >= 0) {
                   NicknamesMain.nickname = (String) lines.get(0);
               } else {
                   options = true;
                   executeWriter = true;
               }
           }

           if (exists(NicknamesMain.USER_DIR + "settings.nn")) {
               BufferedReader f = new BufferedReader(new FileReader(NicknamesMain.USER_DIR + "settings.nn"));

               List lines = f.lines().collect(Collectors.toList());

               if (lines.size() >= 1) {
                   NicknamesMain.useRanks = Boolean.parseBoolean((String) lines.get(0));
                   NicknamesMain.useSkin = Boolean.parseBoolean((String) lines.get(1));
               } else {
                   settings = true;
                   executeWriter = true;
               }
           }

           if (executeWriter) {
               Writer.execute(options, settings);
           }
       } catch (Exception var32) {
           Writer.execute(true, true);
           var32.printStackTrace();
       }
   }

    private static boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }
}
