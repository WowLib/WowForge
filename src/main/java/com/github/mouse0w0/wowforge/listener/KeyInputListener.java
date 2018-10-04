package com.github.mouse0w0.wowforge.listener;

import com.github.mouse0w0.wowforge.gui.GuiKeySetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyInputListener {

    public static final KeyBinding OPEN_KEY_SETTING = new KeyBinding("wowforge.keybinding.openKeySetting", Keyboard.KEY_F7, "wowforge.keybinding.category");

    public static void init() {
        ClientRegistry.registerKeyBinding(OPEN_KEY_SETTING);

        MinecraftForge.EVENT_BUS.register(new KeyInputListener());
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(OPEN_KEY_SETTING.isKeyDown()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiKeySetting(Minecraft.getMinecraft().gameSettings));
        }
    }
}
