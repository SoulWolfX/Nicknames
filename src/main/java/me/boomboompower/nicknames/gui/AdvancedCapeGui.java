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

import me.boomboompower.nicknames.utils.CapeUtils;
import me.boomboompower.nicknames.utils.GlobalUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.net.URL;

//        - 94
//        - 70
//        - 46
//        - 22
//        + 2
//        + 26
//        + 50
//        + 74

public class AdvancedCapeGui extends GuiScreen {

    private GuiTextField textField;
    private GuiButton setButton;

    private GuiScreen previousScreen;

    public AdvancedCapeGui(GuiScreen previous) {
        this.previousScreen = previous;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        textField = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, this.width / 2 - 75, this.height / 2 - 22, 150, 20);

        this.buttonList.add(this.setButton = makeButton(1, "Set cape", this.width / 2 - 75, this.height / 2 + 2));
        this.buttonList.add(makeButton(2, "Back", this.width / 2 - 75, this.height / 2 + 74));

        textField.setMaxStringLength(100);
        textField.setCanLoseFocus(false);
        textField.setFocused(true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        setButton.enabled = textField.getText() != null && !textField.getText().isEmpty();

        textField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                try {
                    if (textField.getText() != null && !textField.getText().isEmpty()) {
                        new URL(textField.getText());

                        CapeUtils.begin(Minecraft.getMinecraft().thePlayer, textField.getText());
                        sendChatMessage("Cape has been updated");
                    } else {
                        sendChatMessage("No characters provided!");
                    }
                } catch (Exception ex) {
                    sendChatMessage("Not a valid url!");
                }
                Minecraft.getMinecraft().displayGuiScreen(null);
                break;
            case 2:
                Minecraft.getMinecraft().displayGuiScreen(previousScreen);
                break;
        }
    }

    @Override
    public void sendChatMessage(String msg) {
        GlobalUtils.sendMessage(msg);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            Minecraft.getMinecraft().displayGuiScreen(previousScreen);
        } else {
            textField.textboxKeyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void display() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    private GuiButton makeButton(int id, String name, int x, int y) {
        return new GuiButton(id, x, y, 150, 20, name);
    }
}
