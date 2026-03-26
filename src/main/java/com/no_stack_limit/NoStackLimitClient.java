package com.no_stack_limit;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public class NoStackLimitClient implements ClientModInitializer {
    private static final Identifier KEY_CATEGORY_ID = Identifier.fromNamespaceAndPath(NoStackLimit.MOD_ID, "controls");
    public static KeyMapping showExactCountKeyBinding;

    @Override
    public void onInitializeClient() {
        showExactCountKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.no_stack_limit.show_exact_count",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_LALT,
                KeyMapping.Category.register(KEY_CATEGORY_ID)
        ));
    }

    public static boolean shouldShowExactCount() {
        if (showExactCountKeyBinding == null) {
            return false;
        }

        if (showExactCountKeyBinding.isDown()) {
            return true;
        }

        Minecraft client = Minecraft.getInstance();
        if (client == null || client.getWindow() == null) {
            return false;
        }

        InputConstants.Key boundKey = KeyBindingHelper.getBoundKeyOf(showExactCountKeyBinding);
        if (boundKey.getType() == InputConstants.Type.KEYSYM || boundKey.getType() == InputConstants.Type.SCANCODE) {
            return InputConstants.isKeyDown(client.getWindow(), boundKey.getValue());
        }

        return false;
    }
}
