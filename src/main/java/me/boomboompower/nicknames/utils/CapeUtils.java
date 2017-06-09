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
import me.boomboompower.nicknames.gui.CapeSelectionGUI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.invoke.MethodHandle;

public class CapeUtils {

    private static final MethodHandle GET_PLAYER_INFO = ReflectUtils.findMethod(AbstractClientPlayer.class, new String[] {"getPlayerInfo", "func_175155_b"});

    public static void begin(AbstractClientPlayer player, CapeSelectionGUI.CapeType type) {
        NicknamesMain.displayedCapeType = type;

        Minecraft.getMinecraft().addScheduledTask(() -> replaceCape(player, type));
    }

    private static void replaceCape(AbstractClientPlayer player, CapeSelectionGUI.CapeType type) {
        NetworkPlayerInfo info = null;

        try {
            info = (NetworkPlayerInfo) GET_PLAYER_INFO.invoke(player);
        } catch (Throwable ex) {
            System.out.println("Issue!");
        }

        if (info == null) {
            System.out.println("playerInfo null, stopping!");
            return;
        }

        ResourceLocation location;
        switch (type) {
            case Y_2016:
                location = getCape("2016");
                break;
            case Y_2015:
                location = getCape("2015");
                break;
            case Y_2013:
                location = getCape("2013");
                break;
            case Y_2012:
                location = getCape("2012");
                break;
            case Y_2011:
                location = getCape("2011");
                break;
            case MOJIRA_MOD:
                location = getCape("tracker");
                break;
            case MOJANG:
                location = getCape("mojang");
                break;
            case MOJANG_CLASSIC:
                location = getCape("classic");
                break;
            case COBALT:
                location = getCape("cobalt");
                break;
            case SCROLLS:
                location = getCape("scrolls");
                break;
            case JULIAN:
                location = getCape("julian");
                break;
            case MILLLION:
                location = getCape("millionth");
                break;
            case MESSIAH:
                location = getCape("messiah");
                break;
            case PRISMARINE:
                location = getCape("prismarine");
                break;
            case CHRISTMAS:
                location = getCape("christmas");
                break;
            default:
                location = null;
                break;
        }

        try {
            ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, info, location, "locationCape", "field_178862_f");
        } catch (Throwable x) {
            x.printStackTrace();
        }
    }

    private static ResourceLocation getCape(String id) {
        return new ResourceLocation(String.format("nicknamesmod:capes/%s.png", id));
    }
}
