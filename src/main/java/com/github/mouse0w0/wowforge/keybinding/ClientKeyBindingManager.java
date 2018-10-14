package com.github.mouse0w0.wowforge.keybinding;

import com.github.mouse0w0.wow.registry.RegistryBase;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ClientKeyBindingManager extends RegistryBase<ClientKeyBinding> {

    private final Multimap<Integer, ClientKeyBinding > hashToKeyBindings = HashMultimap.create();

    @Override
    protected int nextId(ClientKeyBinding clientKeyBinding) {
        return -1;
    }

    public void setId(ClientKeyBinding  keyBinding, int id) {
        idToRegisteredItems.forcePut(id, keyBinding);
        keyToId.forcePut(keyBinding.getRegistryName(), id);
    }

    public void refresh() {

    }
}
