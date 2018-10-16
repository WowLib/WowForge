package com.github.mouse0w0.wowforge.keybinding;

import com.github.mouse0w0.wow.keybinding.Key;
import com.github.mouse0w0.wow.keybinding.KeyDomain;
import com.github.mouse0w0.wow.keybinding.KeyModifier;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeyBindingUtils {

    public static boolean isKeyCodeModifierConflict(Key key1, Key key2, KeyModifier mod1, KeyModifier mod2, KeyDomain domain1, KeyDomain domain2) {
        if (domain1.isConflicts(domain2)) {
            return KeyBindingUtils.isKeyCodeModifierConflict(key1, mod2) ||
                    KeyBindingUtils.isKeyCodeModifierConflict(key2, mod1);
        }
        return false;
    }

    public static boolean isKeyCodeModifierConflict(Key key, KeyModifier keyModifier) {
        switch (keyModifier) {
            case CONTROL:
                return Minecraft.IS_RUNNING_ON_MAC ? key == Key.KEY_LMETA || key == Key.KEY_RMETA : key == Key.KEY_LCONTROL || key == Key.KEY_RCONTROL;
            case SHIFT:
                return key == Key.KEY_LSHIFT || key == Key.KEY_RSHIFT;
            case ALT:
                return key == Key.KEY_LMENU || key == Key.KEY_RMENU;
            default:
                return false;
        }
    }

    public static Key getKeyFromMinecraft(int keyCode) {
        if (keyCode < 0) {
            return Key.valueOfMouse(100 + keyCode);
        } else {
            return Key.valueOf(keyCode < 256 ? keyCode : keyCode - 256);
        }
    }

    public static KeyModifier getKeyModifierFromForge(net.minecraftforge.client.settings.KeyModifier keyModifier) {
        switch (keyModifier) {
            case CONTROL:
                return KeyModifier.CONTROL;
            case ALT:
                return KeyModifier.ALT;
            case SHIFT:
                return KeyModifier.SHIFT;
            default:
                return KeyModifier.NONE;
        }
    }

    public static KeyDomain getKeyDomainFromForge(KeyConflictContext keyConflictContext) {
        switch (keyConflictContext) {
            case GUI:
                return KeyDomain.GUI;
            case IN_GAME:
                return KeyDomain.IN_GAME;
            default:
                return KeyDomain.UNIVERSAL;
        }
    }
}
