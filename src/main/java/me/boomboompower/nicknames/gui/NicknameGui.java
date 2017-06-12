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
import me.boomboompower.nicknames.utils.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;

//        - 94
//        - 70
//        - 46
//        - 22
//        + 2
//        + 26
//        + 50
//        + 74

public class NicknameGui extends GuiScreen {

    int write = -76;
    int one = -46;
    int two = -22;
    int three = 2;
    int four = 26;
    int five = 50;

    int butWidth = 150;
    int butHeight = 20;

    private GuiTextField text;
    private String input = "";

    public NicknameGui() {
        this("");
    }

    public NicknameGui(String input) {
        this.input = input;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        text = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 75, this.height / 2 + write, butWidth, butHeight);

        this.buttonList.add(new GuiButton(1, this.width / 2 - 160, this.height / 2 + one, butWidth, butHeight, "Set Name"));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 10, this.height / 2 + one, butWidth, butHeight, "Reset Name"));

        this.buttonList.add(new GuiButton(3, this.width / 2 - 160, this.height / 2 + two, butWidth, butHeight, "Set Skin"));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 10, this.height / 2 + two, butWidth, butHeight, "Reset Skin"));

        this.buttonList.add(new GuiButton(5, this.width / 2 - 160, this.height / 2 + three, butWidth, butHeight, "Set Cape"));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 10, this.height / 2 + three, butWidth, butHeight, "Reset Cape"));

        this.buttonList.add(new GuiButton(7, this.width / 2 - 160, this.height / 2 + four, butWidth, butHeight, "Use Ranks: " + getRanks()));
        this.buttonList.add(new GuiButton(8, this.width / 2 + 10, this.height / 2 + four, butWidth, butHeight, "Change skin: " + getSkin()));

        this.buttonList.add(new GuiButton(9, this.width / 2 - 160, this.height / 2 + five, butWidth, butHeight, "Modify Gameprofile: " + getProfile()));
        this.buttonList.add(new GuiButton(10, this.width / 2 + 10, this.height / 2 + five, butWidth, butHeight, "Disabled"));

        text.setText(input);
        text.setMaxStringLength(16);
        text.setFocused(true);

        this.buttonList.get(buttonList.size() - 1).enabled = false;
    }
    
    public void display() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    @Override
    public void drawScreen(int x, int y, float ticks) {
        drawDefaultBackground();
        text.drawTextBox();
        drawCenteredString(this.fontRendererObj, "Nicknames", this.width / 2, this.height / 2 - 94, Color.WHITE.getRGB());
        super.drawScreen(x, y, ticks);
    }

    @Override
    protected void keyTyped(char c, int key) {
        try {
            text.textboxKeyTyped(c, key);
            super.keyTyped(c, key);
        } catch (Exception ex) {}
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) {
        try {
            super.mouseClicked(x, y, btn);
            text.mouseClicked(x, y, btn);
        } catch (Exception ex) {}
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        text.updateCursorCounter();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) return;

        boolean closeGui = false;
        switch (button.id) {
            case 0:
                // Dont need to do anything
                break;
            case 1:
                if (text.getText().isEmpty()) {
                    sendChatMessage("No name given!");
                } else if (text.getText().equals(NicknamesMain.userName)) {
                    reset();
                } else if (!GlobalUtils.canSetName(text.getText())) {
                    sendChatMessage("Incorrect format, you may use letters and numbers!");
                } else {
                    set(text.getText());
                }
                mc.displayGuiScreen(null);
                break;
            case 2:
                reset();
                mc.displayGuiScreen(null);
                break;
            case 3:
                if (!text.getText().isEmpty() && text.getText().length() >= 3) {
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
            case 4:
                NicknamesMain.useSkin = false;
                SkinUtils.begin(mc.thePlayer);
                sendChatMessage("Your skin has been reset!");
                mc.displayGuiScreen(null);
                break;
            case 5:
                new CapeSelectionGUI(this).display();
            case 6:
                CapeUtils.begin(mc.thePlayer, CapeSelectionGUI.CapeType.NONE);
                mc.displayGuiScreen(null);
                break;
            case 7:
                NicknamesMain.useRanks = !NicknamesMain.useRanks;
                button.displayString = "Use ranks: " + getRanks();
                break;
            case 8:
                NicknamesMain.useSkin = !NicknamesMain.useSkin;
                button.displayString = "Change skin: " + getSkin();
                break;
            case 9:
                NicknamesMain.useProfile = !NicknamesMain.useProfile;
                button.displayString = "Modify Gameprofile: " + getProfile();
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        Writer.execute(true, true);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void sendChatMessage(String message) {
        GlobalUtils.sendMessage(message);
    }

    private String getRanks() {
        return (NicknamesMain.useRanks ? EnumChatFormatting.GREEN + "Yes" : EnumChatFormatting.RED + "No");
    }

    private String getSkin() {
        return (NicknamesMain.useSkin ? EnumChatFormatting.GREEN + "Yes" : EnumChatFormatting.RED + "No");
    }

    private String getProfile() {
        return (NicknamesMain.useProfile ? EnumChatFormatting.GREEN + "Yes" : EnumChatFormatting.RED + "No");
    }

    private void set(String name) {
        NicknamesMain.nickname = GlobalUtils.translateAlternateColorCodes('&', name);
        sendChatMessage("Your nickname is now " + goldify(name) + "!");

        if (NicknamesMain.useProfile) ProfileUtils.begin(mc.thePlayer);
    }

    private void setSkin(String skinName) {
        NicknamesMain.useSkin = true;
        NicknamesMain.skinName = EnumChatFormatting.getTextWithoutFormattingCodes(GlobalUtils.translateAlternateColorCodes('&', skinName));
        sendChatMessage(String.format("Your skin has been updated to %s!", EnumChatFormatting.GOLD + skinName + EnumChatFormatting.GRAY));

        SkinUtils.begin(mc.thePlayer, false);
    }

    private void reset() {
        NicknamesMain.useSkin = false;
        NicknamesMain.nickname = NicknamesMain.userName;
        sendChatMessage("Your nickname has been reset!");

        ProfileUtils.begin(mc.thePlayer);
        SkinUtils.begin(mc.thePlayer, true);
    }

    private String goldify(String name) {
        return EnumChatFormatting.GOLD + GlobalUtils.translateAlternateColorCodes('&', name) + EnumChatFormatting.GRAY;
    }
}
