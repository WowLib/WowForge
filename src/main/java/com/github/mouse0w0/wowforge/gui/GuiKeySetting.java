package com.github.mouse0w0.wowforge.gui;

import com.github.mouse0w0.wow.keybinding.Key;
import com.github.mouse0w0.wow.keybinding.KeyModifier;
import com.github.mouse0w0.wowforge.WowForge;
import com.github.mouse0w0.wowforge.keybinding.ClientKeyBinding;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

public class GuiKeySetting extends GuiScreen {

    protected String screenTitle = "WowKeySetting";
    public ClientKeyBinding buttonId;
    private GuiKeyBindingList keyBindingList;
    private GuiButton buttonReset;

    public GuiKeySetting() {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        this.keyBindingList = new GuiKeyBindingList(this, this.mc);
        this.buttonList.add(new GuiButton(200, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("gui.done")));
        this.buttonReset = this.addButton(new GuiButton(201, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("controls.resetAll")));
        this.screenTitle = I18n.format("wow.keysetting.title");
//        int i = 0;

//        for (GameSettings.Options gamesettings$options : OPTIONS_ARR)
//        {
//            if (gamesettings$options.isFloat())
//            {
//                this.buttonList.add(new GuiOptionSlider(gamesettings$options.getOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options));
//            }
//            else
//            {
//                this.buttonList.add(new GuiOptionButton(gamesettings$options.getOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options, this.options.getKeyBinding(gamesettings$options)));
//            }
//
//            ++i;
//        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.keyBindingList.handleMouseInput();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 200) {
            this.mc.displayGuiScreen(null);
        } else if (button.id == 201) {
            for (ClientKeyBinding keybinding : WowForge.getKeyBindingManager().getValues()) {
                keybinding.resetToDefault();
            }

            WowForge.getKeyBindingManager().refresh();
        }
//        } else if (button.id < 100 && button instanceof GuiOptionButton) {
//            this.options.setOptionValue(((GuiOptionButton) button).getOption(), 1);
//            button.displayString = this.options.getKeyBinding(GameSettings.Options.byOrdinal(button.id));
//        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.buttonId != null) {
            buttonId.setKey(Key.valueOfMouse(mouseButton));
            buttonId.setKeyModifier(getActiveModifier());
            this.buttonId = null;
            WowForge.getKeyBindingManager().refresh();
        } else if (mouseButton != 0 || !this.keyBindingList.mouseClicked(mouseX, mouseY, mouseButton)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    /**
     * Called when a mouse button is released.
     */
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (state != 0 || !this.keyBindingList.mouseReleased(mouseX, mouseY, state)) {
            super.mouseReleased(mouseX, mouseY, state);
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.buttonId != null) {
            if (keyCode != 0) {
                buttonId.setKey(Key.valueOf(keyCode));
                buttonId.setKeyModifier(getActiveModifier());
            } else if (typedChar > 0) {
                buttonId.setKey(Key.valueOf(typedChar + 256));
                buttonId.setKeyModifier(getActiveModifier());
            }

            if (!net.minecraftforge.client.settings.KeyModifier.isKeyCodeModifier(keyCode))
                this.buttonId = null;
            WowForge.getKeyBindingManager().refresh();
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    private KeyModifier getActiveModifier() {
       return ClientKeyBinding.getActiveModifier();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.keyBindingList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 8, 16777215);
        boolean flag = false;

        for (ClientKeyBinding keybinding : WowForge.getKeyBindingManager().getValues()) {
            if (!keybinding.isDefault()) {
                flag = true;
                break;
            }
        }

        this.buttonReset.enabled = flag;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
