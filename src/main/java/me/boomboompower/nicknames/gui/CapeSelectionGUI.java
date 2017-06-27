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
package me.boomboompower.nicknames.gui;

import me.boomboompower.nicknames.NicknamesMain;
import me.boomboompower.nicknames.utils.CapeUtils;
import me.boomboompower.nicknames.utils.GlobalUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.io.IOException;

//        - 94
//        - 70
//        - 46
//        - 22
//        + 2
//        + 26
//        + 50
//        + 74

public class CapeSelectionGUI extends GuiScreen {

    @Override
    public void initGui() {
        this.buttonList.add(makeButton(0, "Minecon 2016", this.width / 2 - 160, this.height / 2 - 94));
        this.buttonList.add(makeButton(1, "Minecon 2015", this.width / 2 + 10, this.height / 2 - 94));

        this.buttonList.add(makeButton(2, "Minecon 2013", this.width / 2 - 160, this.height / 2 - 70));
        this.buttonList.add(makeButton(3, "Minecon 2012", this.width / 2 + 10, this.height / 2 - 70));

        this.buttonList.add(makeButton(4, "Minecon 2011", this.width / 2 - 160, this.height / 2 - 46));
        this.buttonList.add(makeButton(5, "Mojira Moderator", this.width / 2 + 10, this.height / 2 - 46));

        this.buttonList.add(makeButton(6, "Mojang", this.width / 2 - 160, this.height / 2 - 22));
        this.buttonList.add(makeButton(7, "Mojang (Classic)", this.width / 2 + 10, this.height / 2 - 22));

        this.buttonList.add(makeButton(8, "Cobalt", this.width / 2 - 160, this.height / 2 + 2));
        this.buttonList.add(makeButton(9, "Scrolls", this.width / 2 + 10, this.height / 2 + 2));

        this.buttonList.add(makeButton(10, "JulianClark", this.width / 2 - 160, this.height / 2 + 26));
        this.buttonList.add(makeButton(11, "Millionth", this.width / 2 + 10, this.height / 2 + 26));

        this.buttonList.add(makeButton(12, "MrMessiah", this.width / 2 - 160, this.height / 2 + 50));
        this.buttonList.add(makeButton(13, "Prismarine", this.width / 2 + 10, this.height / 2 + 50));

        this.buttonList.add(makeButton(14, "Advanced", this.width / 2 - 75, this.height / 2 + 74));
        this.buttonList.add(makeButton(15, "Reset Cape", this.width / 2 - 75, this.height / 2 + 98));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        drawCenteredString(this.fontRendererObj, "Cape Selection Menu", this.width / 2, 15, Color.WHITE.getRGB());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        boolean notMenu = true;
        CapeType type = CapeType.NONE;
        switch (button.id) {
            case 0:
                type = CapeType.Y_2016;
                break;
            case 1:
                type = CapeType.Y_2015;
                break;
            case 2:
                type = CapeType.Y_2013;
                break;
            case 3:
                type = CapeType.Y_2012;
                break;
            case 4:
                type = CapeType.Y_2011;
                break;
            case 5:
                type = CapeType.MOJIRA_MOD;
                break;
            case 6:
                type = CapeType.MOJANG;
                break;
            case 7:
                type = CapeType.MOJANG_CLASSIC;
                break;
            case 8:
                type = CapeType.COBALT;
                break;
            case 9:
                type = CapeType.SCROLLS;
                break;
            case 10:
                type = CapeType.JULIAN;
                break;
            case 11:
                type = CapeType.MILLLION;
                break;
            case 12:
                type = CapeType.MESSIAH;
                break;
            case 13:
                type = CapeType.PRISMARINE;
                break;
            case 14:
                notMenu = false;
                new AdvancedCapeGui(this).display();
                break;
            case 15:
                notMenu = false;
                CapeUtils.begin(mc.thePlayer, CapeType.NONE);
                sendChatMessage("Your cape has been reset!");
                break;
            default:
                type = CapeType.NONE;
                break;
        }
        if (notMenu) {
            CapeUtils.begin(mc.thePlayer, type);
            GlobalUtils.sendMessage("Cape changed successfully!");
            mc.displayGuiScreen(null);
        }
    }

    public enum CapeType {
        NONE,
        Y_2016,
        Y_2015,
        Y_2013,
        Y_2012,
        Y_2011,
        MOJIRA_MOD,
        MOJANG,
        MOJANG_CLASSIC,
        COBALT,
        SCROLLS,
        JULIAN,
        MILLLION,
        MESSIAH,
        PRISMARINE,
        CHRISTMAS
    }

    public void display() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onGuiClosed() {
        NicknamesMain.fileUtils.saveConfig();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    private GuiButton makeButton(int id, String name, int x, int y) {
        return new GuiButton(id, x, y, 150, 20, name);
    }
}