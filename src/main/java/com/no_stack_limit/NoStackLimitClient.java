package com.no_stack_limit;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class NoStackLimitClient implements ClientModInitializer {
    private static final Identifier KEY_CATEGORY_ID = Identifier.of(NoStackLimit.MOD_ID, "controls");
    public static KeyBinding showExactCountKeyBinding;

    @Override
    public void onInitializeClient() {
        showExactCountKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.no_stack_limit.show_exact_count",
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_LEFT_ALT,
                KeyBinding.Category.create(KEY_CATEGORY_ID)
        ));
    }

    public static boolean shouldShowExactCount() {
        return showExactCountKeyBinding != null && showExactCountKeyBinding.isPressed();
    }
}
