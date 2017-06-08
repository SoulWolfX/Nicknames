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

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.boomboompower.nicknames.NicknamesMain;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.invoke.MethodHandle;

public class SkinUtils {

    private static Minecraft mc;

    private static final ResourceLocation ALEX = new ResourceLocation("textures/entity/alex.png");
    private static final ResourceLocation STEVE = new ResourceLocation("textures/entity/steve.png");

    private static final MethodHandle GET_PLAYER_INFO = ReflectUtils.findMethod(AbstractClientPlayer.class, new String[] {"getPlayerInfo", "func_175155_b"});
    private static final MethodHandle GET_PLAYER_SKIN = ReflectUtils.findFieldGetter(NetworkPlayerInfo.class, "locationSkin", "field_178865_e");

    public static void begin(AbstractClientPlayer player) {
        if (!NicknamesMain.isEnabled()) return;

        mc = Minecraft.getMinecraft();

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
                skinToUse = loadSkin(player); //NicknamesMain.hasDefaultSkin() ? NicknamesMain.defaultSkin : STEVE;
        }

        try {
            ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, info, skinToUse, "locationSkin", "field_178865_e");
        } catch (Throwable x) {
            x.printStackTrace();
        }
    }

    public static ResourceLocation loadSkin(AbstractClientPlayer player) {
        final ResourceLocation resourcelocation = new ResourceLocation("skins/" + NicknamesMain.userName);
        ITextureObject itextureobject = mc.renderEngine.getTexture(resourcelocation);

        File file1 = new File(new File(".", "skins"), NicknamesMain.userName.length() > 2 ? NicknamesMain.userName.substring(0, 2) : "xx");
        File file2 = new File(file1,NicknamesMain.userName);
        final IImageBuffer iimagebuffer = new ImageBufferDownload();

        ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(file2, String.format("https://minotar.net/skin/%s", NicknamesMain.userName), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer() {
            public BufferedImage parseUserSkin(BufferedImage image) {
                if (iimagebuffer != null) {
                    image = iimagebuffer.parseUserSkin(image);
                }

                return image;
            }
            public void skinAvailable() {
                if (iimagebuffer != null)
                {
                    iimagebuffer.skinAvailable();
                }
            }
        });
        mc.renderEngine.loadTexture(resourcelocation, threaddownloadimagedata);

        return resourcelocation;
    }
}
