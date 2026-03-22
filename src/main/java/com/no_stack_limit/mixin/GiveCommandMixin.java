package com.no_stack_limit.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.command.GiveCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GiveCommand.class)
public abstract class GiveCommandMixin {
    /**
     * @author Codex
     * @reason Avoid integer overflow in the vanilla maxStackSize * 100 limit check.
     */
    @Overwrite
    private static int execute(ServerCommandSource source, ItemStackArgument item, Collection<ServerPlayerEntity> targets, int count) throws CommandSyntaxException {
        ItemStack previewStack = item.createStack(1, false);
        int maxCount = previewStack.getMaxCount();
        int giveLimit = noStackLimit$getGiveLimit(maxCount);
        if (count > giveLimit) {
            source.sendError(Text.translatable("commands.give.failed.toomanyitems", giveLimit, previewStack.toHoverableText()));
            return 0;
        }

        for (ServerPlayerEntity player : targets) {
            int remaining = count;

            while (remaining > 0) {
                int stackAmount = Math.min(maxCount, remaining);
                remaining -= stackAmount;

                ItemStack givenStack = item.createStack(stackAmount, false);
                boolean inserted = player.getInventory().insertStack(givenStack);
                if (!inserted || !givenStack.isEmpty()) {
                    ItemEntity itemEntity = player.dropItem(givenStack, false);
                    if (itemEntity != null) {
                        itemEntity.resetPickupDelay();
                        itemEntity.setOwner(player.getUuid());
                    }
                    continue;
                }

                ItemEntity itemEntity = player.dropItem(previewStack, false);
                if (itemEntity != null) {
                    itemEntity.setDespawnImmediately();
                }

                player.getEntityWorld().playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.ENTITY_ITEM_PICKUP,
                        SoundCategory.PLAYERS,
                        0.2F,
                        ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
                );
                ScreenHandler screenHandler = player.currentScreenHandler;
                screenHandler.sendContentUpdates();
            }
        }

        if (targets.size() == 1) {
            ServerPlayerEntity target = targets.iterator().next();
            source.sendFeedback(
                    () -> Text.translatable("commands.give.success.single", count, previewStack.toHoverableText(), target.getDisplayName()),
                    true
            );
        } else {
            source.sendFeedback(
                    () -> Text.translatable("commands.give.success.multiple", count, previewStack.toHoverableText(), targets.size()),
                    true
            );
        }

        return targets.size();
    }

    @Unique
    private static int noStackLimit$getGiveLimit(int maxCount) {
        return (int)Math.min((long)maxCount * 100L, Integer.MAX_VALUE);
    }
}
