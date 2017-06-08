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
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.invoke.MethodHandle;

public class SkinUtils {

    private static final ResourceLocation ALEX = new ResourceLocation("textures/entity/alex.png");
    private static final ResourceLocation STEVE = new ResourceLocation("textures/entity/steve.png");

    private static final MethodHandle GET_PLAYER_INFO = ReflectUtils.findMethod(AbstractClientPlayer.class, new String[] {"getPlayerInfo", "func_175155_b"});
    private static final MethodHandle GET_PLAYER_SKIN = ReflectUtils.findFieldGetter(NetworkPlayerInfo.class, "locationSkin", "field_178865_e");

    public static void begin(AbstractClientPlayer player) {
        if (!NicknamesMain.isEnabled()) return;

        Minecraft.getMinecraft().addScheduledTask(() -> replaceSkin(player));
    }

    private static void replaceSkin(AbstractClientPlayer player) {
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

        ResourceLocation skinToUse;
        switch (NicknamesMain.skinType) {
            case ALEX:
                skinToUse = ALEX;
                break;
            case STEVE:
                skinToUse = STEVE;
                break;
            case BOOMBOOMPOWER:
                skinToUse = new ResourceLocation("nicknamesmod:boomboompower.png");
                break;
            case STONE:
                skinToUse = new ResourceLocation("nicknamesmod:stone.png");
                break;
            default:
                skinToUse = NicknamesMain.hasDefaultSkin() ? NicknamesMain.defaultSkin : STEVE;
        }

        try {
            ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, info, skinToUse, "locationSkin", "field_178865_e");
        } catch (Throwable x) {
            x.printStackTrace();
        }
    }
}
