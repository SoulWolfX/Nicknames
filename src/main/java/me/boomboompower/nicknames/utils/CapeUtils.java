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
import me.boomboompower.nicknames.gui.CapeGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.invoke.MethodHandle;

public class CapeUtils {

    private static final MethodHandle GET_PLAYER_INFO = ReflectUtils.findMethod(AbstractClientPlayer.class, new String[] {"getPlayerInfo", "func_175155_b"});

    public static void begin(AbstractClientPlayer player, String s) {
        Minecraft.getMinecraft().addScheduledTask(() -> replaceCape(player, CapeGui.CapeType.NONE, s));
    }

    public static void begin(AbstractClientPlayer player, CapeGui.CapeType type) {
        NicknamesMain.displayedCapeType = type;

        Minecraft.getMinecraft().addScheduledTask(() -> replaceCape(player, type, null));
    }

    private static void replaceCape(AbstractClientPlayer player, CapeGui.CapeType type, String s) {
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

        if (s != null) {
            try {
                location = downloadCape(s);
            } catch (Exception ex) {
                GlobalUtils.sendMessage("Could not set custom cape...");
            }
        }

        try {
            ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, info, location, "locationCape", "field_178862_f");
        } catch (Throwable x) {
            x.printStackTrace();
        }
    }

    private static ResourceLocation getCape(String url) {
        return new ResourceLocation(String.format("nicknamesmod:capes/%s.png", url));
    }

    private static ResourceLocation downloadCape(String url) {
        if (url != null && !url.isEmpty()) {
            final ResourceLocation rl = new ResourceLocation("capeof/" + url);

            TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

            IImageBuffer iib = new IImageBuffer() {
                ImageBufferDownload ibd = new ImageBufferDownload();
                public BufferedImage parseUserSkin(BufferedImage var1) {
                    return CapeUtils.parseCape(var1);
                }

                @Override
                public void skinAvailable() {

                }
            };
            ThreadDownloadImageData textureCape = new ThreadDownloadImageData(null, url, null, iib);
            textureManager.loadTexture(rl, textureCape);

            return rl;
        } else {
            return null;
        }
    }

    public static BufferedImage parseCape(BufferedImage img) {
        int imageWidth = 64;
        int imageHeight = 32;
        int srcWidth = img.getWidth();

        for (int srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageHeight *= 2) {
            imageWidth *= 2;
        }

        BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
        Graphics g = imgNew.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return imgNew;
    }
}
