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

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GlobalUtils {

    public static final String PREFIX = EnumChatFormatting.AQUA + "N" + EnumChatFormatting.BLUE + "N" + EnumChatFormatting.DARK_GRAY + " > " + EnumChatFormatting.GRAY;

    public static void sendMessage(String message) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(PREFIX + translateAlternateColorCodes('&', message)));
    }

    public static boolean canSetName(String username) {
        return (username != null) && (username.matches("^[a-zA-Z0-9&_]*$")) && (username.length() >= 3) && (username.length() <= 16) && (!username.equals(NicknamesMain.userName));
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
                b[i] = '\u00A7';
                b[i+1] = Character.toLowerCase(b[i+1]);
            }
        }
        return new String(b);
    }

    public static List<String> getOnlinePlayerNames() {
        List<String> names = new ArrayList<String>();
        for (EntityPlayer entity : Minecraft.getMinecraft().theWorld.playerEntities) {
            names.add(entity.getName());
        }
        return names;
    }
}
