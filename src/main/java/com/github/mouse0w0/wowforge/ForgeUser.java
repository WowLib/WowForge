package com.github.mouse0w0.wowforge;

import com.github.mouse0w0.wow.WowPlatform;
import com.github.mouse0w0.wow.profile.User;
import net.minecraft.client.Minecraft;

import java.util.UUID;

public class ForgeUser implements User {

    private final UUID userUuid = fromString(Minecraft.getMinecraft().getSession().getPlayerID());

    private static UUID fromString(final String input) {
        try {
            return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        }catch (IllegalArgumentException e) {
            WowForge.getLogger().warn("Unknown player's uuid.", e);
            return null;
        }
    }

    @Override
    public boolean isSupport() {
        return true;
    }

    @Override
    public int getVersion() {
        return WowPlatform.getInternalVersion();
    }

    @Override
    public UUID getUUID() {
        return userUuid;
    }

    @Override
    public String getName() {
        return Minecraft.getMinecraft().getSession().getUsername();
    }

    @Override
    public Object getSource() {
        return Minecraft.getMinecraft().getSession();
    }
}
