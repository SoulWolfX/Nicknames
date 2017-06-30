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

import me.boomboompower.nicknames.command.CapeCommand;
import me.boomboompower.nicknames.command.NicknameCommand;
import me.boomboompower.nicknames.command.SkinCommand;
import me.boomboompower.nicknames.events.NicknameEvents;
import me.boomboompower.nicknames.gui.CapeGui;
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
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.text.WordUtils;

@Mod(modid = NicknamesMain.MODID, version = NicknamesMain.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "*")
public class NicknamesMain {

    public static final String MODID = "nicknamesmod";
    public static final String VERSION = "1.3.3";

    public static String USER_DIR;

    public static FileUtils fileUtils;

    public static Boolean useProfile = false;
    public static Boolean useRanks = false;
    public static Boolean useSkin = false;

    public static String skinName;
    public static String nickname;
    public static String userName = "username";

    public static int currentTick = 0;

    public static Object un;
    public static Object nn;
    public static Object sn;
    public static Object ct;

    public static CapeGui.CapeType displayedCapeType = CapeGui.CapeType.NONE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModMetadata data = event.getModMetadata();
        data.version = VERSION;
        data.name = EnumChatFormatting.GOLD + "Hypixel " + EnumChatFormatting.GRAY + "-" + EnumChatFormatting.DARK_RED + " Nicknames";
        data.authorList.add("boomboompower");
        data.description = "Allows you to nickname yourself! (Clientside) |" + EnumChatFormatting.RESET + " Made with " + EnumChatFormatting.LIGHT_PURPLE + "<3" + EnumChatFormatting.RESET + " by boomboompower";

        fileUtils = new FileUtils(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        userName = Minecraft.getMinecraft().getSession().getUsername();

        registerEvents(this, new NicknameEvents());
        registerCommands(new NicknameCommand(), new SkinCommand(), new CapeCommand());

        Minecraft.getMinecraft().addScheduledTask(() -> fileUtils.loadConfig());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        try {
            if (Class.forName("me.boomboompower.textdisplayer.loading.Placeholder") != null) {
                un = new me.boomboompower.textdisplayer.loading.Placeholder("nn_un", nickname != null ? nickname : "Player");
                nn = new me.boomboompower.textdisplayer.loading.Placeholder("nn_nn", nickname != null ? nickname : userName);
                sn = new me.boomboompower.textdisplayer.loading.Placeholder("nn_sn", skinName != null ? skinName : "None");
                sn = new me.boomboompower.textdisplayer.loading.Placeholder("nn_ct", displayedCapeType != null ? WordUtils.capitalizeFully(displayedCapeType.name()) : CapeGui.CapeType.NONE.name());
            }
        } catch (Throwable ex) {
            System.out.println("Issue registering placeholder, nvm");
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().currentScreen == null) {
            if (currentTick > 0) {
                --currentTick;
            } else {
                if (displayedCapeType != CapeGui.CapeType.NONE) {
                    CapeUtils.begin(Minecraft.getMinecraft().thePlayer, displayedCapeType);
                }
                if (useSkin) {
                    SkinUtils.begin(Minecraft.getMinecraft().thePlayer, skinName, false);
                }
                currentTick = 100;
            }
            if (nn != null && nickname != null) {
                try {
                    ((me.boomboompower.textdisplayer.loading.Placeholder) un).setReplacement(userName != null ? userName : "Player");
                    ((me.boomboompower.textdisplayer.loading.Placeholder) nn).setReplacement(nickname != null ? nickname : userName);
                    ((me.boomboompower.textdisplayer.loading.Placeholder) sn).setReplacement(skinName != null ? skinName : "None");
                    ((me.boomboompower.textdisplayer.loading.Placeholder) sn).setReplacement(displayedCapeType != null ? WordUtils.capitalizeFully(displayedCapeType.name()) : CapeGui.CapeType.NONE.name());
                } catch (Exception ex) {
                }
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
        return !(NicknamesMain.nickname == null || NicknamesMain.nickname.equals(NicknamesMain.userName));
    }
}
