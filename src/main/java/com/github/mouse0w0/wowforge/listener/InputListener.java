package com.github.mouse0w0.wowforge.listener;

import com.github.mouse0w0.wow.WowPlatform;
import com.github.mouse0w0.wow.keybinding.Key;
import com.github.mouse0w0.wowforge.WowForge;
import com.github.mouse0w0.wowforge.gui.GuiKeySetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputListener {

    public static final KeyBinding OPEN_KEY_SETTING = new KeyBinding("wow.keybinding.openKeySetting", Keyboard.KEY_F7, "wow.keybinding.category");

    public static void init() {
        ClientRegistry.registerKeyBinding(OPEN_KEY_SETTING);

        MinecraftForge.EVENT_BUS.register(new InputListener());
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!WowForge.getCurrentServer().isSupport()) {
            return;
        }

        if (OPEN_KEY_SETTING.isKeyDown()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiKeySetting());
        }

        int keyCode = Keyboard.getEventKey();
        boolean pressed = Keyboard.getEventKeyState();
        WowForge.getKeyBindingManager().handle(keyCode, pressed);
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (!WowForge.getCurrentServer().isSupport()) {
            return;
        }

        int mouseCode = Mouse.getEventButton();
        boolean pressed = Mouse.getEventButtonState();
        WowForge.getKeyBindingManager().handle(mouseCode + Key.MOUSE_BUTTON_LEFT.getCode(), pressed);
    }
}
