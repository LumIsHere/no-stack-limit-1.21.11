package com.no_stack_limit.mixin.client;

import com.no_stack_limit.TooltipHelper;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class HandledScreenMixin {
    @Shadow
    protected Slot hoveredSlot;

    @Shadow
    protected AbstractContainerMenu menu;

    @Shadow
    protected abstract List<Component> getTooltipFromContainerItem(ItemStack stack);

    @Shadow
    private boolean showTooltipWithItemInHand(ItemStack stack) {
        throw new AssertionError();
    }

    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    private void noStackLimit$drawExactCountTooltip(GuiGraphics context, int x, int y, CallbackInfo ci) {
        if (this.hoveredSlot == null || !this.hoveredSlot.hasItem()) {
            return;
        }

        ItemStack stack = this.hoveredSlot.getItem();
        if (!this.menu.getCarried().isEmpty() && !this.showTooltipWithItemInHand(stack)) {
            return;
        }

        List<Component> tooltip = TooltipHelper.appendExactCount(this.getTooltipFromContainerItem(stack), stack);
        Identifier tooltipStyle = stack.get(DataComponents.TOOLTIP_STYLE);
        context.setTooltipForNextFrame(Minecraft.getInstance().font, tooltip, stack.getTooltipImage(), x, y, tooltipStyle);
        ci.cancel();
    }
}
