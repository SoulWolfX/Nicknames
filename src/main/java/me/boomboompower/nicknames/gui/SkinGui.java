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
import me.boomboompower.nicknames.gui.utils.TextBox;
import me.boomboompower.nicknames.utils.GlobalUtils;
import me.boomboompower.nicknames.utils.SkinUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

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

public class SkinGui extends GuiScreen {

    private String previousString;
    private String previousName;
    private boolean setSkin;

    private TextBox text;
    private String input;

    public SkinGui() {
        this("");
    }

    public SkinGui(String input) {
        this.previousName = NicknamesMain.skinName;
        this.previousString = "";
        this.input = input;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        this.text = new TextBox(0, this.width / 2 - 100, this.height / 2 - 56, 200, 20);
        this.buttonList.add(new GuiButton(1, this.width / 2 - 75, this.height / 2 - 22, 150, 20, "Set skin"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 75, this.height / 2 + 2, 150, 20, "Reset skin"));

        text.setText(input);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        GuiInventory.drawEntityOnScreen(this.width / 2 + 150, this.height / 2, 30, this.width / 2 + 150 - mouseX, (this.height / 2 - 50) - mouseY, mc.thePlayer);
        drawCenteredString(fontRendererObj, "Skin preview", this.width / 2 + 150, this.height / 2 + 10, Color.WHITE.getRGB());

        text.drawTextBox();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                if (!text.getText().isEmpty() && text.getText().length() >= 2) {
                    boolean doable = true;
                    for (char c : text.getText().toCharArray()) {
                        if (!Character.isLetterOrDigit(c) && c != '_') {
                            doable = false;
                        }
                    }
                    if (doable) {
                        setSkin(text.getText());
                    } else {
                        sendChatMessage("Name contains invalid characters!");
                    }
                } else {
                    sendChatMessage("Not enough characters provided!");
                }
                mc.displayGuiScreen(null);
                break;
            case 2:
                previousName = NicknamesMain.userName;
                NicknamesMain.useSkin = false;
                NicknamesMain.skinName = null;

                SkinUtils.begin(mc.thePlayer);

                sendChatMessage("Your skin has been reset!");
                mc.displayGuiScreen(null);
                break;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) { // Close the screen on Esc key
            mc.displayGuiScreen(null);
        } else {
            text.textboxKeyTyped(typedChar, keyCode);
            if (!text.getText().isEmpty() && text.getText().length() >= 2 && !text.getText().equals(previousString)) { // If the text isn't empty, its more than 2 letters and its not the same as the previous name, update
                SkinUtils.begin(mc.thePlayer, text.getText(), false);
                previousString = text.getText();
            }
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) {
        try {
            super.mouseClicked(x, y, btn);
            text.mouseClicked(x, y, btn);
        } catch (Exception ex) {}
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        if (setSkin) { // Only save the config if the skin has changed
            NicknamesMain.fileUtils.saveConfig();
            SkinUtils.begin(mc.thePlayer, text.getText(), false);
        } else {
            SkinUtils.begin(mc.thePlayer, previousName, false); // Revert to previous skin
        }
    }

    @Override
    public void sendChatMessage(String msg) {
        GlobalUtils.sendMessage(msg);
    }

    public void display() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    private void setSkin(String skinName) {
        if (NicknamesMain.skinName.equals(skinName)) return; // Don't update skin if it matches the previous one

        setSkin = true; // Config will now be saved
        NicknamesMain.skinName = EnumChatFormatting.getTextWithoutFormattingCodes(GlobalUtils.translateAlternateColorCodes('&', skinName));
        sendChatMessage(String.format("Your skin has been updated to %s!", EnumChatFormatting.GOLD + skinName + EnumChatFormatting.GRAY));
    }
}
