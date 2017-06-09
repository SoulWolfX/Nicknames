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
import me.boomboompower.nicknames.gui.CapeSelectionGUI;
import me.boomboompower.nicknames.utils.CapeUtils;
import me.boomboompower.nicknames.utils.FileUtils;
import me.boomboompower.nicknames.utils.SkinUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;

@Mod(modid = NicknamesMain.MODID, version = NicknamesMain.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "*")
public class NicknamesMain {

    public static final String MODID = "nicknamesmod";
    public static final String VERSION = "1.2.0";

    public static String USER_DIR;

    public static Boolean useRanks = true;
    public static Boolean useSkin = false;

    public static String skinName = "Notch";
    public static String userName = "username";
    public static String nickname = "nickname";

    public static int currentTick = 0;

    public static CapeSelectionGUI.CapeType displayedCapeType = CapeSelectionGUI.CapeType.NONE;

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
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().currentScreen == null) {
            if (currentTick > 0) {
                --currentTick;
            } else {
                if (displayedCapeType != CapeSelectionGUI.CapeType.NONE) {
                    CapeUtils.begin(Minecraft.getMinecraft().thePlayer, displayedCapeType);
                }
                if (useSkin) {
                    SkinUtils.begin(Minecraft.getMinecraft().thePlayer, false);
                }
                currentTick = 100;
            }
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
}
