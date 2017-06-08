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
import me.boomboompower.nicknames.utils.GlobalUtils;
import me.boomboompower.nicknames.utils.ProfileUtils;
import me.boomboompower.nicknames.utils.SkinUtils;
import me.boomboompower.nicknames.utils.Writer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
        text = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 75, this.height / 2 - 58, 150, 20);
        this.buttonList.add(new GuiButton(1, this.width / 2 - 75, this.height / 2 - 22, 150, 20, "Set"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 75, this.height / 2 + 2, 150, 20, "Reset"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 75, this.height / 2 + 26, 150, 20, "Use Ranks: " + getRanks()));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 75, this.height / 2 + 50, 150, 20, "Current Skin: " + getSkin()));

        text.setText(input);
        text.setMaxStringLength(16);
        text.setFocused(true);
    }
    
    public void display() {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        FMLCommonHandler.instance().bus().unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    @Override
    public void drawScreen(int x, int y, float ticks) {
        drawDefaultBackground();
        text.drawTextBox();
        drawCenteredString(this.fontRendererObj, "Nicknames", this.width / 2, this.height / 2 - 82, Color.WHITE.getRGB());
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
                Minecraft.getMinecraft().displayGuiScreen(null);
                break;
            case 2:
                reset();
                Minecraft.getMinecraft().displayGuiScreen(null);
                break;
            case 3:
                NicknamesMain.useRanks = !NicknamesMain.useRanks;
                button.displayString = "Use ranks: " + getRanks();
                break;
            case 4:
                switch (NicknamesMain.skinType) {
                    case STEVE:
                        NicknamesMain.skinType = NicknamesMain.SkinType.ALEX;
                        break;
                    case ALEX:
                        NicknamesMain.skinType = NicknamesMain.SkinType.BOOMBOOMPOWER;
                        break;
                    case BOOMBOOMPOWER:
                        NicknamesMain.skinType = NicknamesMain.SkinType.STONE;
                        break;
                    case STONE:
                        NicknamesMain.skinType = NicknamesMain.SkinType.NONE;
                        break;
                    default:
                        NicknamesMain.skinType = NicknamesMain.SkinType.STEVE;
                        break;
                }
                button.displayString = "Current Skin: " + getSkin();
                SkinUtils.begin(mc.thePlayer);
        }
    }

    @Override
    public void onGuiClosed() {
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
        return (compareOG(NicknamesMain.SkinType.SkinOriginality.ORIGINAL) ? EnumChatFormatting.GREEN : compareOG(NicknamesMain.SkinType.SkinOriginality.CUSTOM) ? EnumChatFormatting.GOLD : EnumChatFormatting.RED) + (compareType(NicknamesMain.SkinType.STEVE) ? "Steve" : compareType(NicknamesMain.SkinType.ALEX) ? "Alex" : compareType(NicknamesMain.SkinType.BOOMBOOMPOWER) ? "boom" : compareType(NicknamesMain.SkinType.STONE) ? "Stone" : "None");
    }

    private boolean compareType(NicknamesMain.SkinType type) {
        return NicknamesMain.skinType == type;
    }

    private boolean compareOG(NicknamesMain.SkinType.SkinOriginality originality) {
        return NicknamesMain.skinType.originality == originality;
    }

    private void set(String name) {
        NicknamesMain.nickname = GlobalUtils.translateAlternateColorCodes('&', name);
        sendChatMessage("Your nickname is now " + goldify(name) + "!");

        ProfileUtils.begin(mc.thePlayer);
        SkinUtils.begin(mc.thePlayer);
    }

    private void reset() {
        NicknamesMain.skinType = NicknamesMain.SkinType.NONE;
        NicknamesMain.nickname = NicknamesMain.userName;
        sendChatMessage("Your nickname has been reset!");

        ProfileUtils.begin(mc.thePlayer);
        SkinUtils.begin(mc.thePlayer);
    }

    private String goldify(String name) {
        return EnumChatFormatting.GOLD + GlobalUtils.translateAlternateColorCodes('&', name) + EnumChatFormatting.GRAY;
    }
}
