package com.github.mouse0w0.wowforge.keybinding;

import com.github.mouse0w0.wow.WowPlatform;
import com.github.mouse0w0.wow.keybinding.KeyDomain;
import com.github.mouse0w0.wow.keybinding.KeyModifier;
import com.github.mouse0w0.wow.network.packet.client.KeyBindingActionPacket;
import com.github.mouse0w0.wow.registry.RegistryBase;
import com.github.mouse0w0.wow.util.GsonUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;

import java.nio.file.Path;

public class ClientKeyBindingManager extends RegistryBase<ClientKeyBinding> {

    private final Multimap<Integer, ClientKeyBinding> keyCodeToKeyBindings = HashMultimap.create();

    @Override
    protected int nextId(ClientKeyBinding clientKeyBinding) {
        return -1;
    }

    public void setId(ClientKeyBinding keyBinding, int id) {
        idToRegisteredItems.forcePut(id, keyBinding);
        keyToId.forcePut(keyBinding.getRegistryName(), id);
    }

    public void clear() {
        registeredItems.clear();
        idToRegisteredItems.clear();
        keyToId.clear();
    }

    public void refresh() {
        keyCodeToKeyBindings.clear();
        for (ClientKeyBinding keyBinding : getValues()) {
            keyCodeToKeyBindings.put(keyBinding.getKey().getCode(), keyBinding);
        }
    }

    public void handle(int code, boolean pressed) {
        KeyModifier activeModifier = ClientKeyBinding.getActiveModifier();
        KeyDomain activeDomain = getActiveDomain();
        for (ClientKeyBinding keyBinding : keyCodeToKeyBindings.get(code)) {
            if(keyBinding.getKeyModifier() == activeModifier && activeDomain.isConflicts(keyBinding.getDomain())) {
                if(keyBinding.isPressed() == pressed)
                    continue;
                keyBinding.setPressed(pressed);
                WowPlatform.getNetwork().send(null, new KeyBindingActionPacket(getId(keyBinding), pressed));
            }
        }
    }

    public void loadConfig(Path configFile) {

    }

    public void saveConfig(Path configFile) {

    }

    public static KeyDomain getActiveDomain() {
        return Minecraft.getMinecraft().currentScreen != null ? KeyDomain.GUI : KeyDomain.IN_GAME;
    }
}
