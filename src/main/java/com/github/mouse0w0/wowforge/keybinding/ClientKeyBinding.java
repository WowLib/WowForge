package com.github.mouse0w0.wowforge.keybinding;

import com.github.mouse0w0.wow.keybinding.Key;
import com.github.mouse0w0.wow.keybinding.KeyBinding;
import com.github.mouse0w0.wow.keybinding.KeyDomain;
import com.github.mouse0w0.wow.keybinding.KeyModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.input.Keyboard;

public class ClientKeyBinding extends KeyBinding<ClientKeyBinding> {

    private Key key;
    private KeyModifier keyModifier;
    private boolean pressed;

    public ClientKeyBinding(Key defaultKey, KeyModifier defaultModifier, KeyDomain domain, String displayName) {
        super(defaultKey, defaultModifier, domain, displayName);
        resetToDefault();
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public KeyModifier getKeyModifier() {
        return keyModifier;
    }

    public void setKeyModifier(KeyModifier keyModifier) {
        this.keyModifier = keyModifier;
    }

    public void resetToDefault() {
        setKey(getDefaultKey());
        setKeyModifier(getDefaultModifier());
    }

    public boolean isDefault() {
        return getKey() == getDefaultKey() && getKeyModifier() == getDefaultModifier();
    }

    public String getKeyDisplayName() {
        switch (getKeyModifier()) {
            case CONTROL: {
                String localizationFormatKey = Minecraft.IS_RUNNING_ON_MAC ? "forge.controlsgui.control.mac" : "forge.controlsgui.control";
                return I18n.format(localizationFormatKey, getKeyDisplayName(getKey().getCode()));
            }
            case SHIFT: {
                return I18n.format("forge.controlsgui.shift", getKeyDisplayName(getKey().getCode()));
            }
            case ALT: {
                return I18n.format("forge.controlsgui.alt", getKeyDisplayName(getKey().getCode()));
            }
            default: {
                return getKeyDisplayName(getKey().getCode());
            }
        }
    }

    private String getKeyDisplayName(int key) {
        if (key >= 240)
        {
            switch (key)
            {
                case 240:
                    return I18n.format("key.mouse.left");
                case 241:
                    return I18n.format("key.mouse.right");
                case 242:
                    return I18n.format("key.mouse.middle");
                default:
                    return I18n.format("key.mouseButton", key - Key.MOUSE_BUTTON_LEFT.getCode() + 1);
            }
        }
        else
        {
            return key < 256 ? Keyboard.getKeyName(key) : String.format("%c", (char)(key - 256)).toUpperCase();
        }
    }

    public boolean isConflicts(ClientKeyBinding other) {
        return getKey() == other.getKey() && getKeyModifier() == other.getKeyModifier() && getDomain().isConflicts(other.getDomain());
    }

    public static KeyModifier getActiveModifier() {
        if(GuiScreen.isCtrlKeyDown())
            return KeyModifier.CONTROL;
        else if(GuiScreen.isShiftKeyDown())
            return KeyModifier.SHIFT;
        else if(GuiScreen.isAltKeyDown())
            return KeyModifier.ALT;
        else
            return KeyModifier.NONE;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}
