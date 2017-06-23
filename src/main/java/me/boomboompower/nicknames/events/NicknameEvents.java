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
        if (NicknamesMain.nickname != null && NicknamesMain.isEnabled()) {
            if (event.message.getFormattedText().contains(NicknamesMain.userName)) {
                String formatted = event.message.getFormattedText();

                if (NicknamesMain.useRanks && !formatted.contains(" joined")) {
                    if (NicknamesMain.nickname.startsWith("§6")) {
                        event.message = build(getFormat(formatted, "[YT] ", EnumChatFormatting.GOLD));
                    } else if (NicknamesMain.nickname.startsWith("§b")) {
                        event.message = build(getFormat(formatted, "[MVP] ", EnumChatFormatting.AQUA));
                    } else if (NicknamesMain.nickname.startsWith("§c")) {
                        event.message = build(getFormat(formatted, "[ADMIN] ", EnumChatFormatting.RED));
                    } else if (NicknamesMain.nickname.startsWith("§a")) {
                        event.message = build(getFormat(formatted, "[VIP] ", EnumChatFormatting.GREEN));
                    } else if (NicknamesMain.nickname.startsWith("§2")) {
                        event.message = build(getFormat(formatted, "[MOD] ", EnumChatFormatting.DARK_GREEN));
                    } else if (NicknamesMain.nickname.startsWith("§9")) {
                        event.message = build(getFormat(formatted, "[HELPER] ", EnumChatFormatting.BLUE));
                    } else {
                        event.message = build(formatted(formatted));
                    }
                } else {
                    event.message = build(formatted(formatted));
                }
            }
        }
    }

    /*

      - TRUE FORMATS
    &b[MVP&c+&b] jeff&f: HI!
    &b[MVP] jeff&f: HI!

      - WHAT YOU SEE
    [SHOUT] [MVP] jeff: HI!
    [MVP] jeff: HI!
    jeff: HI!

     */

    public ChatComponentText build(String message) {
        return new ChatComponentText(message);
    }

    public String getFormat(String message, String prefix, EnumChatFormatting color) {
        message = formatted(message);

        if (EnumChatFormatting.getTextWithoutFormattingCodes(message).startsWith("[") && message.contains("]")) {
            String[] split = message.split("]");
            try {
                message = (split[1] + split[2]).replaceFirst(" ", "");
            } catch (Exception ex) {
                message = split[1].replaceFirst(" ", "");
            }
        }

        return color + prefix + message;
    }

    public String formatted(String message) {
        return message.replace(NicknamesMain.userName, NicknamesMain.nickname);
    }
}
