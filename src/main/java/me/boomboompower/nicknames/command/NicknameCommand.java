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
package me.boomboompower.nicknames.command;

import me.boomboompower.nicknames.gui.NicknameGui;
import me.boomboompower.nicknames.utils.GlobalUtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NicknameCommand implements ICommand {

    @Override
    public String getCommandName() {
        return "nickname";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return EnumChatFormatting.RED + "Usage: /nickname";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("nn", "nickname");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            new NicknameGui().display();
        } else {
            new NicknameGui(args[0]).display();
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandBase.getListOfStringsMatchingLastWord(args, GlobalUtils.getOnlinePlayerNames()) : new ArrayList<String>();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

//            // COMMAND DEBUG LINES
//
//            if (args[0].equalsIgnoreCase("__reset")) {
//                SkinUtils.begin(Minecraft.getMinecraft().thePlayer, true);
//                GlobalUtils.sendMessage("Reset. Run \"/nn __get\" for more usage info!");
//            } else if (args[0].equalsIgnoreCase("__get")) {
//                Minecraft mc = Minecraft.getMinecraft();
//                GlobalUtils.sendMessage(String.format("isEnabled: %s", NicknamesMain.isEnabled()));
//                GlobalUtils.sendMessage(String.format("hasSkin: %s", mc.thePlayer.getLocationSkin() != null));
//                GlobalUtils.sendMessage(String.format("useRanks: %s", NicknamesMain.useRanks));
//                GlobalUtils.sendMessage(String.format("useSkin: %s", NicknamesMain.useSkin));
//                GlobalUtils.sendMessage("");
//                GlobalUtils.sendMessage(String.format("USER_DIR: %s", NicknamesMain.USER_DIR));
//                GlobalUtils.sendMessage("");
//                GlobalUtils.sendMessage(String.format("displayedCapeType: %s", NicknamesMain.displayedCapeType));
//                GlobalUtils.sendMessage("");
//                GlobalUtils.sendMessage(String.format("skinLoaction: %s", mc.thePlayer.getLocationSkin() != null ? mc.thePlayer.getLocationSkin() : "NONE"));
//                GlobalUtils.sendMessage(String.format("capeLocation: %s", mc.thePlayer.getLocationCape() != null ? mc.thePlayer.getLocationCape() : "NONE"));
//                GlobalUtils.sendMessage("");
//                GlobalUtils.sendMessage(String.format("hasSkin: %s", mc.thePlayer.getLocationSkin() != null));
//            } else if (args[0].equalsIgnoreCase("__deletecache")) {
//                try {
//                    FileUtils.deleteDirectory(new File(NicknamesMain.USER_DIR + "skins"));
//                } catch (Exception ex) {}
//                GlobalUtils.sendMessage(String.format("Attempted delete. stillExists = [%s]", new File(".", "skins").exists()));
//            }
}
