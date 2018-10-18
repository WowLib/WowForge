package com.github.mouse0w0.wowforge.network.handler;

import com.github.mouse0w0.wow.keybinding.Key;
import com.github.mouse0w0.wow.keybinding.KeyDomain;
import com.github.mouse0w0.wow.keybinding.KeyModifier;
import com.github.mouse0w0.wow.network.PacketHandler;
import com.github.mouse0w0.wow.network.packet.server.RegisterKeyBindingPacket;
import com.github.mouse0w0.wowforge.WowForge;
import com.github.mouse0w0.wowforge.keybinding.ClientKeyBinding;
import com.github.mouse0w0.wowforge.keybinding.ClientKeyBindingManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class RegisterKeyBindingPacketHandler implements PacketHandler<RegisterKeyBindingPacket> {
    @Override
    public void handle(Object o, RegisterKeyBindingPacket packet) {
        JsonArray keyBindings = packet.getJson();
        ClientKeyBindingManager keyBindingManager = WowForge.getKeyBindingManager();
        keyBindingManager.clear();
        for(JsonElement json: keyBindings) {
            JsonObject jo = json.getAsJsonObject();
            ClientKeyBinding keyBinding = new ClientKeyBinding(Key.valueOf(jo.get("key").getAsInt()),
                    KeyModifier.values()[jo.get("mod").getAsInt()],
                    KeyDomain.values()[jo.get("domain").getAsInt()],
                    jo.get("display").getAsString())
                    .setRegistryName(jo.get("name").getAsString());
            keyBindingManager.register(keyBinding);
            keyBindingManager.setId(keyBinding, jo.get("id").getAsInt());
        }
        keyBindingManager.init();
    }
}
