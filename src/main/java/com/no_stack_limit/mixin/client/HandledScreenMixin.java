package com.no_stack_limit.mixin.client;

import com.no_stack_limit.TooltipHelper;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {
    @Shadow
    protected Slot focusedSlot;

    @Shadow
    protected ScreenHandler handler;

    @Shadow
    protected abstract List<Text> getTooltipFromItem(ItemStack stack);

    @Shadow
    private boolean isItemTooltipSticky(ItemStack stack) {
        throw new AssertionError();
    }

    @Inject(method = "drawMouseoverTooltip", at = @At("HEAD"), cancellable = true)
    private void noStackLimit$drawExactCountTooltip(DrawContext context, int x, int y, CallbackInfo ci) {
        if (this.focusedSlot == null || !this.focusedSlot.hasStack()) {
            return;
        }

        ItemStack stack = this.focusedSlot.getStack();
        if (!this.handler.getCursorStack().isEmpty() && !this.isItemTooltipSticky(stack)) {
            return;
        }

        List<Text> tooltip = TooltipHelper.appendExactCount(this.getTooltipFromItem(stack), stack);
        Identifier tooltipStyle = stack.get(DataComponentTypes.TOOLTIP_STYLE);
        context.drawTooltip(MinecraftClient.getInstance().textRenderer, tooltip, stack.getTooltipData(), x, y, tooltipStyle);
        ci.cancel();
    }
}
