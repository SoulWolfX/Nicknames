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

import com.mojang.authlib.GameProfile;

import me.boomboompower.nicknames.NicknamesMain;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.invoke.MethodHandle;

public class ProfileUtils {

    private static final MethodHandle GET_GAMEPROFILE = ReflectUtils.findMethod(EntityPlayer.class, new String[] {"getGameProfile", "func_146103_bH"});

    public static void begin(AbstractClientPlayer player) {
        if (!NicknamesMain.isEnabled()) return;

        Minecraft.getMinecraft().addScheduledTask(() -> changeName(player));
    }

    private static void changeName(AbstractClientPlayer player) {
        GameProfile profile = null;

        try {
            profile = (GameProfile) GET_GAMEPROFILE.invoke(player);
        } catch (Throwable ex) {
            System.out.println("Issue!");
        }

        if (profile == null) {
            System.out.println("playerInfo null, stopping!");
            return;
        }

        try {
            ObfuscationReflectionHelper.setPrivateValue(GameProfile.class, profile, NicknamesMain.nickname, "name");
            Minecraft.getMinecraft().ingameGUI.getTabList().updatePlayerList(false);
        } catch (Throwable x) {
            x.printStackTrace();
        }
    }
}
