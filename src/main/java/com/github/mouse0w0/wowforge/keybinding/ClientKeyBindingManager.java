package com.github.mouse0w0.wowforge.keybinding;

import com.github.mouse0w0.wow.WowPlatform;
import com.github.mouse0w0.wow.keybinding.Key;
import com.github.mouse0w0.wow.keybinding.KeyDomain;
import com.github.mouse0w0.wow.keybinding.KeyModifier;
import com.github.mouse0w0.wow.network.packet.client.KeyBindingActionPacket;
import com.github.mouse0w0.wow.registry.RegistryBase;
import com.github.mouse0w0.wow.util.GsonUtils;
import com.github.mouse0w0.wowforge.WowForge;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Files;
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

    public void init() {
        refreshKeyToBindings();
        loadConfig();
    }

    public void refreshKeyToBindings() {
        keyCodeToKeyBindings.clear();
        for (ClientKeyBinding keyBinding : getValues()) {
            keyCodeToKeyBindings.put(keyBinding.getKey().getCode(), keyBinding);
        }
    }

    public void handle(int code, boolean pressed) {
        KeyModifier activeModifier = ClientKeyBinding.getActiveModifier();
        KeyDomain activeDomain = getActiveDomain();
        for (ClientKeyBinding keyBinding : keyCodeToKeyBindings.get(code)) {
            if (keyBinding.getKeyModifier() == activeModifier && activeDomain.isConflicts(keyBinding.getDomain())) {
                if (keyBinding.isPressed() == pressed)
                    continue;
                keyBinding.setPressed(pressed);
                WowPlatform.getNetwork().send(null, new KeyBindingActionPacket(getId(keyBinding), pressed));
            }
        }
    }

    public void loadConfig() {
        Path configFile = WowForge.getServerConfigPath().resolve("keyBindings.json");
        if (!Files.exists(configFile)) {
            return;
        }

        try (InputStream inputStream = Files.newInputStream(configFile)) {
            loadConfigFromJson(GsonUtils.JSON_PARSER.parse(new InputStreamReader(inputStream)).getAsJsonObject());
        } catch (IOException e) {
            WowForge.getLogger().warn(e.getMessage(), e);
        }
    }

    public void loadConfigFromJson(JsonObject jsonObject) {
        for (ClientKeyBinding keyBinding : getValues()) {
            String registryName = keyBinding.getRegistryName().toString();
            if (jsonObject.has(registryName)) {
                JsonObject keyBindingProperties = jsonObject.get(registryName).getAsJsonObject();
                keyBinding.setKey(Key.valueOf(keyBindingProperties.get("key").getAsString()));
                keyBinding.setKeyModifier(KeyModifier.valueOf(keyBindingProperties.get("mod").getAsString()));
            }
        }
    }

    public void saveConfig() {
        Path configFile = WowForge.getServerConfigPath().resolve("keyBindings.json");
        try {
            if (!Files.exists(configFile)) {
                if (!Files.exists(configFile.getParent())) {
                    Files.createDirectories(configFile.getParent().toAbsolutePath());
                }
                Files.createFile(configFile);
            }

            try (Writer writer = Files.newBufferedWriter(configFile)) {
                writer.write(saveConfigToJson().toString());
            }
        } catch (IOException e) {
            WowForge.getLogger().warn(e.getMessage(), e);
        }
    }

    public JsonObject saveConfigToJson() {
        JsonObject jsonObject = new JsonObject();
        for (ClientKeyBinding keyBinding : getValues()) {
            String registryName = keyBinding.getRegistryName().toString();
            JsonObject keyBindingProperties = new JsonObject();
            keyBindingProperties.addProperty("key", keyBinding.getKey().name());
            keyBindingProperties.addProperty("mod", keyBinding.getKeyModifier().name());
            jsonObject.add(registryName, keyBindingProperties);
        }
        return jsonObject;
    }

    public static KeyDomain getActiveDomain() {
        return Minecraft.getMinecraft().currentScreen != null ? KeyDomain.GUI : KeyDomain.IN_GAME;
    }
}
