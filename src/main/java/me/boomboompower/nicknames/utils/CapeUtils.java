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
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.invoke.MethodHandle;

public class CapeUtils {

    private static Minecraft mc;

    private static final MethodHandle GET_PLAYER_INFO = ReflectUtils.findMethod(AbstractClientPlayer.class, new String[] {"getPlayerInfo", "func_175155_b"});

    public static void begin(AbstractClientPlayer player) {
        begin(player, true);
    }

    public static void begin(AbstractClientPlayer player, boolean reset) {
        if (!NicknamesMain.isEnabled()) return;

        mc = Minecraft.getMinecraft();

        Minecraft.getMinecraft().addScheduledTask(() -> replaceCape(player, reset));
    }

    private static void replaceCape(AbstractClientPlayer player, boolean reset) {
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
        if (reset) {
            location = null;
        } else {
            location = loadCape(null);
        }

        try {
            ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, info, location, "locationCape", "field_178862_f");
            System.out.println("STABBED!");
            System.out.println("HMMMM \"" + location + "\"");
            System.out.println("INFO \"" + info.getLocationCape() + "\"");
        } catch (Throwable x) {
            x.printStackTrace();
        }


    }

    public static ResourceLocation loadCape(String username) {

        ResourceLocation location = new ResourceLocation("nicknamesmod:capes/star.png");

        mc.renderEngine.bindTexture(location);

        return location;

//        final ResourceLocation resourceLocation = new ResourceLocation("capes/" + username);
//        ITextureObject textureObject = mc.renderEngine.getTexture(resourceLocation);
//
//        File skinsDirectory = new File(new File(".", "capes"), username.length() > 2 ? username.substring(0, 2) : "xx");
//        File downloadedSkinLocation = new File(skinsDirectory, username);
//        final IImageBuffer imageBuffer = new ImageBufferDownload();
//
//        ThreadDownloadImageData imageData = new ThreadDownloadImageData(downloadedSkinLocation, String.format("https://minotar.net/skin/%s", username), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer() {
//
//            public BufferedImage parseUserSkin(BufferedImage image) {
//                if (imageBuffer != null) {
//                    image = imageBuffer.parseUserSkin(image);
//                }
//                return image;
//            }
//            public void skinAvailable() {
//                if (imageBuffer != null) {
//                    imageBuffer.skinAvailable();
//                }
//            }
//        });
//        mc.renderEngine.loadTexture(resourceLocation, imageData);
//
//        return resourceLocation;
    }
}
