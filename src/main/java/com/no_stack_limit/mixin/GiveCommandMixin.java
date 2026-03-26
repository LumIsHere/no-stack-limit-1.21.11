package com.no_stack_limit.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
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
    private static int giveItem(CommandSourceStack source, ItemInput item, Collection<ServerPlayer> targets, int count) throws CommandSyntaxException {
        ItemStack previewStack = item.createItemStack(1, false);
        int maxCount = previewStack.getMaxStackSize();
        int giveLimit = noStackLimit$getGiveLimit(maxCount);
        if (count > giveLimit) {
            source.sendFailure(Component.translatable("commands.give.failed.toomanyitems", giveLimit, previewStack.getDisplayName()));
            return 0;
        }

        for (ServerPlayer player : targets) {
            int remaining = count;

            while (remaining > 0) {
                int stackAmount = Math.min(maxCount, remaining);
                remaining -= stackAmount;

                ItemStack givenStack = item.createItemStack(stackAmount, false);
                boolean inserted = player.getInventory().add(givenStack);
                if (!inserted || !givenStack.isEmpty()) {
                    ItemEntity itemEntity = player.drop(givenStack, false);
                    if (itemEntity != null) {
                        itemEntity.setNoPickUpDelay();
                        itemEntity.setTarget(player.getUUID());
                    }
                    continue;
                }

                ItemEntity itemEntity = player.drop(previewStack, false);
                if (itemEntity != null) {
                    itemEntity.makeFakeItem();
                }

                player.level().playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.ITEM_PICKUP,
                        SoundSource.PLAYERS,
                        0.2F,
                        ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
                );
                AbstractContainerMenu screenHandler = player.containerMenu;
                screenHandler.broadcastChanges();
            }
        }

        if (targets.size() == 1) {
            ServerPlayer target = targets.iterator().next();
            source.sendSuccess(
                    () -> Component.translatable("commands.give.success.single", count, previewStack.getDisplayName(), target.getDisplayName()),
                    true
            );
        } else {
            source.sendSuccess(
                    () -> Component.translatable("commands.give.success.multiple", count, previewStack.getDisplayName(), targets.size()),
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
