package com.no_stack_limit.mixin.client;

import com.no_stack_limit.StackCountFormatter;
import java.lang.reflect.Field;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public abstract class DrawContextMixin {
    @Unique
    private static final float NO_STACK_LIMIT$COUNT_SCALE = 0.7F;

    @Shadow
    public abstract Matrix3x2fStack pose();

    @Shadow
    public abstract void text(Font textRenderer, String text, int x, int y, int color, boolean shadow);

    @Inject(method = "itemCount", at = @At("HEAD"), cancellable = true, require = 0)
    private void noStackLimit$drawScaledCount(Font textRenderer, ItemStack stack, int x, int y, String stackCountText, CallbackInfo ci) {
        noStackLimit$drawScaledCountInternal(textRenderer, stack, x, y, stackCountText, ci);
    }

    @Unique
    private void noStackLimit$drawScaledCountInternal(Font textRenderer, ItemStack stack, int x, int y, String stackCountText, CallbackInfo ci) {
        int count = noStackLimit$getStackCount(stack);
        String countText = noStackLimit$getCountText(count, stackCountText);
        if (countText == null) {
            return;
        }

        float scale = NO_STACK_LIMIT$COUNT_SCALE;
        float scaledWidth = noStackLimit$getTextWidth(textRenderer, countText) * scale;
        float drawX = x + 17 - scaledWidth;
        float drawY = y + 17 - noStackLimit$getLineHeight(textRenderer) * scale;
        Matrix3x2fStack matrices = noStackLimit$getMatrices();

        matrices.pushMatrix();
        matrices.scale(scale, scale);
        this.text(textRenderer, countText, Math.round(drawX / scale), Math.round(drawY / scale), -1, true);
        matrices.popMatrix();
        ci.cancel();
    }

    @Unique
    private Matrix3x2fStack noStackLimit$getMatrices() {
        return this.pose();
    }

    @Unique
    private static int noStackLimit$getStackCount(ItemStack stack) {
        return stack.getCount();
    }

    @Unique
    private static int noStackLimit$getTextWidth(Font textRenderer, String text) {
        return textRenderer.width(text);
    }

    @Unique
    private static int noStackLimit$getLineHeight(Font textRenderer) {
        return ((Number)noStackLimit$getFieldAny(textRenderer, new String[]{"fontHeight", "field_2000", "lineHeight"})).intValue();
    }

    @Unique
    private static Object noStackLimit$getFieldAny(Object target, String[] names) {
        ReflectiveOperationException last = null;
        for (String name : names) {
            try {
                Field field = noStackLimit$findField(target.getClass(), name);
                field.setAccessible(true);
                return field.get(target);
            } catch (ReflectiveOperationException e) {
                last = e;
            }
        }

        throw new RuntimeException("Failed to read any of " + String.join(", ", names), last);
    }

    @Unique
    private static Field noStackLimit$findField(Class<?> type, String name) throws NoSuchFieldException {
        Class<?> current = type;
        while (current != null) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(name);
    }

    @Unique
    private static String noStackLimit$getCountText(int count, String stackCountText) {
        if (StackCountFormatter.shouldCompact(count)) {
            return StackCountFormatter.formatCompact(count);
        }
        if (stackCountText != null) {
            return stackCountText;
        }
        if (count != 1) {
            return Integer.toString(count);
        }
        return null;
    }
}
