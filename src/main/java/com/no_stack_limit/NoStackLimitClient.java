package com.no_stack_limit;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;

public class NoStackLimitClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
    }

    public static boolean shouldShowExactCount() {
        Minecraft client = Minecraft.getInstance();
        if (client == null || client.getWindow() == null) {
            return false;
        }

        return InputConstants.isKeyDown(client.getWindow(), InputConstants.KEY_LALT)
                || InputConstants.isKeyDown(client.getWindow(), InputConstants.KEY_RALT);
    }
}
