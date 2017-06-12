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

import java.io.FileWriter;
import java.io.IOException;

public class Writer implements Runnable {

    private static String ls = System.lineSeparator();

    private static Boolean isOptions;
    private static Boolean isSettings;

    public Writer() {
    }

    public static void execute(boolean options, boolean settings) {
        isOptions = options;
        isSettings = settings;

        (new Thread(new Writer())).start();
    }

    public void run() {
        if (isOptions) {
            try {
                FileWriter e = new FileWriter(NicknamesMain.USER_DIR + "options.nn");

                this.write(e, NicknamesMain.nickname + ls);

                e.close();
            } catch (Throwable var56) {
                var56.printStackTrace();
            }
        }

        if (isSettings) {
            try {
                FileWriter e = new FileWriter(NicknamesMain.USER_DIR + "settings.nn");

                this.write(e, NicknamesMain.useRanks + ls);
                this.write(e, NicknamesMain.useSkin + ls);
                this.write(e, NicknamesMain.useProfile + ls);

                e.close();
            } catch (Throwable var56) {
                var56.printStackTrace();
            }
        }
    }

    private void write(FileWriter writer, String text) {
        try {
            writer.write(text);
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }
}