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
package me.boomboompower.nicknames.events;

import me.boomboompower.nicknames.NicknamesMain;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NicknameEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onChatRecieve(ClientChatReceivedEvent event) {
        if (event.message == null) return;
        if (NicknamesMain.isEnabled()) {
            if (event.message.getFormattedText().contains(NicknamesMain.userName)) {
                if (NicknamesMain.useRanks && !event.message.getFormattedText().contains(" joined the ")) {
                    if (NicknamesMain.nickname.startsWith("§6")) {
                        event.message = new ChatComponentText(EnumChatFormatting.GOLD + "[YT] " + formatted(removeRank(event.message.getFormattedText())));
                    } else if (NicknamesMain.nickname.startsWith("§b")) {
                        event.message = new ChatComponentText(EnumChatFormatting.AQUA + "[MVP] " + formatted(removeRank(event.message.getFormattedText())));
                    } else if (NicknamesMain.nickname.startsWith("§c")) {
                        event.message = new ChatComponentText(EnumChatFormatting.RED + "[ADMIN] " + formatted(removeRank(event.message.getFormattedText())));
                    } else if (NicknamesMain.nickname.startsWith("§a")) {
                        event.message = new ChatComponentText(EnumChatFormatting.GREEN + "[VIP] " + formatted(removeRank(event.message.getFormattedText())));
                    } else if (NicknamesMain.nickname.startsWith("§2")) {
                        event.message = new ChatComponentText(EnumChatFormatting.DARK_GREEN + "[MOD] " + formatted(removeRank(event.message.getFormattedText())));
                    } else if (NicknamesMain.nickname.startsWith("§9")) {
                        event.message = new ChatComponentText(EnumChatFormatting.BLUE + "[HELPER] " + formatted(removeRank(event.message.getFormattedText())));
                    } else {
                        event.message = new ChatComponentText(formatted(event.message.getFormattedText()));
                    }
                } else {
                    event.message = new ChatComponentText(formatted(event.message.getFormattedText()));
                }
            }
        }
    }

    public boolean isRank(String message) {
        return message.startsWith("[MVP] ") || message.startsWith("[MVP+] ") || message.startsWith("[VIP] ") || message.startsWith("[VIP+] ");
    }

    /*

    [SHOUT] [MVP] jeff: HI!
    [MVP] jeff: HI!
    jeff: HI!

     */

    public String removeRank(String message) {
        if (message.contains("]")) {
            return message.split("]")[1].replaceFirst(" ", "");
        }
        return message;
    }

    public String formatted(String message) {
        return message.replace(NicknamesMain.userName, NicknamesMain.nickname);
    }
}
