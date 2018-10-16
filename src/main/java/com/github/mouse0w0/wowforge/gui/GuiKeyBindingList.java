package com.github.mouse0w0.wowforge.gui;

import com.github.mouse0w0.wow.keybinding.Key;
import com.github.mouse0w0.wow.keybinding.KeyDomain;
import com.github.mouse0w0.wow.keybinding.KeyModifier;
import com.github.mouse0w0.wowforge.WowForge;
import com.github.mouse0w0.wowforge.keybinding.ClientKeyBinding;
import com.github.mouse0w0.wowforge.keybinding.ClientKeyBindingManager;
import com.github.mouse0w0.wowforge.keybinding.KeyBindingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.Comparator;

public class GuiKeyBindingList extends GuiListExtended {
    private final GuiKeySetting controlsScreen;
    private final Minecraft mc;
    private final GuiListExtended.IGuiListEntry[] listEntries;
    private int maxListLabelWidth;

    public GuiKeyBindingList(GuiKeySetting controls, Minecraft mcIn) {
        super(mcIn, controls.width + 45, controls.height, 24, controls.height - 32, 20);
        this.controlsScreen = controls;
        this.mc = mcIn;
        ClientKeyBindingManager manager = WowForge.getKeyBindingManager();
        ClientKeyBinding[] keyBindings = manager.getValues().toArray(new ClientKeyBinding[0]);
        this.listEntries = new GuiListExtended.IGuiListEntry[keyBindings.length];
        Arrays.sort(keyBindings, Comparator.comparingInt(manager::getId));
        int i = 0;

        for (ClientKeyBinding keybinding : keyBindings) {
            this.listEntries[i++] = new KeyEntry(keybinding);
        }
    }

    protected int getSize() {
        return this.listEntries.length;
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public GuiListExtended.IGuiListEntry getListEntry(int index) {
        return this.listEntries[index];
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 35;
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth() {
        return super.getListWidth() + 32;
    }

    @SideOnly(Side.CLIENT)
    public class KeyEntry implements GuiListExtended.IGuiListEntry {
        /**
         * The keybinding specified for this KeyEntry
         */
        private final ClientKeyBinding keybinding;
        /**
         * The localized key description for this KeyEntry
         */
        private final String keyDesc;
        private final GuiButton btnChangeKeyBinding;
        private final GuiButton btnReset;

        private KeyEntry(ClientKeyBinding name) {
            this.keybinding = name;
            this.keyDesc = name.getDisplayName();
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 95, 20, name.getKeyDisplayName());
            this.btnReset = new GuiButton(0, 0, 0, 50, 20, I18n.format("controls.reset"));
        }

        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
            boolean flag = GuiKeyBindingList.this.controlsScreen.buttonId == this.keybinding;
            GuiKeyBindingList.this.mc.fontRenderer.drawString(this.keyDesc, x, y + slotHeight / 2 - GuiKeyBindingList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
            this.btnReset.x = x + 210;
            this.btnReset.y = y;
            this.btnReset.enabled = !this.keybinding.isDefault();
            this.btnReset.drawButton(GuiKeyBindingList.this.mc, mouseX, mouseY, partialTicks);
            this.btnChangeKeyBinding.x = x + 105;
            this.btnChangeKeyBinding.y = y;
            this.btnChangeKeyBinding.displayString = this.keybinding.getKeyDisplayName();
            boolean flag1 = false;
            boolean keyCodeModifierConflict = false; // less severe form of conflict, like SHIFT conflicting with SHIFT+G

            if (this.keybinding.getKey().getCode() != 0) {
                for (ClientKeyBinding other : WowForge.getKeyBindingManager().getValues()) {
                    if (other != this.keybinding && other.isConflicts(this.keybinding)) {
                        flag1 = true;
                        keyCodeModifierConflict = isKeyCodeModifierConflict(this.keybinding, other);
                        break;
                    }
                }

                for (KeyBinding other : Minecraft.getMinecraft().gameSettings.keyBindings) {
                    Key mcKey = KeyBindingUtils.getKeyFromMinecraft(other.getKeyCode());
                    KeyModifier mcKeyModifier = KeyBindingUtils.getKeyModifierFromForge(other.getKeyModifier());
                    KeyDomain mcKeyDomain = KeyBindingUtils.getKeyDomainFromForge((KeyConflictContext) other.getKeyConflictContext());
                    if (isConflictsWithMineCraftKeyBinding(this.keybinding, mcKey, mcKeyModifier, mcKeyDomain)) {
                        flag1 = true;
                        keyCodeModifierConflict = isKeyCodeModifierConflict(this.keybinding, mcKey, mcKeyModifier, mcKeyDomain);
                    }
                }
            }

            if (flag) {
                this.btnChangeKeyBinding.displayString = TextFormatting.WHITE + "> " + TextFormatting.YELLOW + this.btnChangeKeyBinding.displayString + TextFormatting.WHITE + " <";
            } else if (flag1) {
                this.btnChangeKeyBinding.displayString = (keyCodeModifierConflict ? TextFormatting.GOLD : TextFormatting.RED) + this.btnChangeKeyBinding.displayString;
            }

            this.btnChangeKeyBinding.drawButton(GuiKeyBindingList.this.mc, mouseX, mouseY, partialTicks);
        }

        private boolean isConflictsWithMineCraftKeyBinding(ClientKeyBinding keyBinding, Key mcKey, KeyModifier mcKeyModifier, KeyDomain mcKeyDomain) {
            return mcKey == keyBinding.getKey() && mcKeyModifier == keyBinding.getKeyModifier() && mcKeyDomain.isConflicts(mcKeyDomain);
        }

        private boolean isKeyCodeModifierConflict(ClientKeyBinding keyBinding, Key mcKey, KeyModifier mcKeyModifier, KeyDomain mcKeyDomain) {
            return KeyBindingUtils.isKeyCodeModifierConflict(keyBinding.getKey(), mcKey, keyBinding.getKeyModifier(), mcKeyModifier, keyBinding.getDomain(), mcKeyDomain);
        }

        private boolean isKeyCodeModifierConflict(ClientKeyBinding keyBinding, ClientKeyBinding other) {
            return KeyBindingUtils.isKeyCodeModifierConflict(keyBinding.getKey(), other.getKey(), keyBinding.getKeyModifier(), other.getKeyModifier(), keyBinding.getDomain(), other.getDomain());
        }

        /**
         * Called when the mouse is clicked within this entry. Returning true means that something within this entry was
         * clicked and the list should not be dragged.
         */
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            if (this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.this.mc, mouseX, mouseY)) {
                GuiKeyBindingList.this.controlsScreen.buttonId = this.keybinding;
                return true;
            } else if (this.btnReset.mousePressed(GuiKeyBindingList.this.mc, mouseX, mouseY)) {
                this.keybinding.resetToDefault();
                KeyBinding.resetKeyBindingArrayAndHash();
                return true;
            } else {
                return false;
            }
        }

        /**
         * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
         */
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            this.btnChangeKeyBinding.mouseReleased(x, y);
            this.btnReset.mouseReleased(x, y);
        }

        public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
        }
    }
}
