package com.no_stack_limit;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class TooltipHelper {
    private TooltipHelper() {
    }

    public static List<Text> appendExactCount(List<Text> tooltip, ItemStack stack) {
        if (!StackCountFormatter.shouldCompact(stack.getCount()) || !NoStackLimitClient.shouldShowExactCount()) {
            return tooltip;
        }

        List<Text> updatedTooltip = new ArrayList<>(tooltip);
        updatedTooltip.add(Text.translatable("tooltip.no_stack_limit.exact_count", StackCountFormatter.formatExact(stack.getCount()))
                .formatted(Formatting.GRAY));
        return updatedTooltip;
    }
}
