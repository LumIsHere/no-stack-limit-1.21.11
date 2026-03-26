package com.no_stack_limit;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class TooltipHelper {
    private TooltipHelper() {
    }

    public static List<Component> appendExactCount(List<Component> tooltip, ItemStack stack) {
        if (!StackCountFormatter.shouldCompact(stack.getCount()) || !NoStackLimitClient.shouldShowExactCount()) {
            return tooltip;
        }

        List<Component> updatedTooltip = new ArrayList<>(tooltip);
        updatedTooltip.add(Component.translatable("tooltip.no_stack_limit.exact_count", StackCountFormatter.formatExact(stack.getCount()))
                .withStyle(ChatFormatting.GRAY));
        return updatedTooltip;
    }
}
