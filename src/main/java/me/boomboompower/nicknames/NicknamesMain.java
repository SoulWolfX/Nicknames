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

package me.boomboompower.nicknames;

import me.boomboompower.nicknames.command.NicknameCommand;
import me.boomboompower.nicknames.events.NicknameEvents;
import me.boomboompower.nicknames.utils.FileUtils;
import me.boomboompower.nicknames.utils.SkinUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.command.ICommand;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.File;

@Mod(modid = NicknamesMain.MODID, version = NicknamesMain.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "*")
public class NicknamesMain {

    public static final String MODID = "nicknamesmod";
    public static final String VERSION = "1.2.0";

    public static String USER_DIR;

    public static SkinType skinType = SkinType.NONE;

    public static Boolean useRanks = true;

    public static String userName = "username";
    public static String nickname = "nickname";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModMetadata data = event.getModMetadata();
        data.version = VERSION;
        data.name = EnumChatFormatting.GOLD + "Hypixel " + EnumChatFormatting.GRAY + "-" + EnumChatFormatting.DARK_RED + " Nicknames";
        data.authorList.add("boomboompower");
        data.description = "Allows you to nickname yourself! (Clientside) |" + EnumChatFormatting.RESET + " Made with " + EnumChatFormatting.LIGHT_PURPLE + "<3" + EnumChatFormatting.RESET + " by boomboompower";
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        registerEvents(this, new NicknameEvents());

        registerCommands(new NicknameCommand());

        USER_DIR = "mods" + File.separator + "nicknames" + File.separator + Minecraft.getMinecraft().getSession().getProfile().getId() + File.separator;

        userName = Minecraft.getMinecraft().getSession().getUsername();

        try {
            FileUtils.getVars();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRender(RenderLivingEvent.Pre event) {
        if (event.entity instanceof EntityPlayerSP && NicknamesMain.isEnabled()) {
            SkinUtils.begin(Minecraft.getMinecraft().thePlayer);
        }
    }

    private void registerCommands(ICommand... command) {
        for (ICommand iCommand : command) {
            try {
                ClientCommandHandler.instance.registerCommand(iCommand);
            } catch (Exception ex) {
                // Shouldn't happen
                ex.printStackTrace();
            }
        }
    }

    private void registerEvents(Object... events) {
        for (Object event : events) {
            MinecraftForge.EVENT_BUS.register(event);
        }
    }

    public static boolean isEnabled() {
        return !NicknamesMain.nickname.equals(NicknamesMain.userName);
    }

    public enum SkinType {
        NONE(SkinOriginality.NORMAL),
        STEVE(SkinOriginality.ORIGINAL),
        ALEX(SkinOriginality.ORIGINAL),
        STONE(SkinOriginality.CUSTOM),
        BOOMBOOMPOWER(SkinOriginality.CUSTOM);

        public SkinOriginality originality;

        SkinType(SkinOriginality originality) {
            this.originality = originality;
        }

        public enum SkinOriginality {
            ORIGINAL,
            NORMAL,
            CUSTOM
        }
    }
}
