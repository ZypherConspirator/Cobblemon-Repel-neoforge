package com.sniklz.repelreforged.item.custom;

import com.sniklz.repelreforged.Config;
import com.sniklz.repelreforged.RepelReforged;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.awt.*;
import java.util.List;

public class RepelBlockItem extends BlockItem {

    private final int BlockLevel;

    public RepelBlockItem(Block block, Properties properties, int blockLevel) {
        super(block, properties);
        this.BlockLevel = blockLevel;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        int blockRange = 1;
        switch (BlockLevel) {
            case 1: blockRange = Config.REPEL_RANGE_1.get(); break;
            case 2: blockRange = Config.REPEL_RANGE_2.get(); break;
            case 3: blockRange = Config.REPEL_RANGE_3.get(); break;
        }

        MutableComponent coloredRange = Component.literal(String.valueOf(blockRange)).withColor(Color.magenta.getRGB());
        MutableComponent chunkCounterComponent;
        int chunkCounter;
        if(blockRange > 16) {
            chunkCounter = (int)Math.floor((double) blockRange / 16); //16 blocks in chunk
            chunkCounterComponent = Component.translatable(
                    "message.repelreforged.in_chunk", chunkCounter, chunkCounter).withColor(Color.magenta.getRGB());
        } else {
            chunkCounterComponent = Component.literal("");
        }

        tooltipComponents.add(Component.translatable(
                "message.repelreforged.repel_tooltip", coloredRange, chunkCounterComponent).withColor(Color.gray.getRGB()));
        tooltipComponents.add(Component.translatable(
                "message.repelreforged.repel_right_click_tooltip").withColor(Color.gray.getRGB()));
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        Player player = context.getPlayer();

        if(!context.getLevel().isClientSide) {
            if (RepelReforged.isRepelNearby((ServerLevel) context.getLevel(), context.getClickedPos())) {
                player.displayClientMessage(Component.translatable(
                        "message.repelreforged.nearby_message").withColor(0xFF0000), true);
            } else {
                player.displayClientMessage(Component.translatable(
                        "message.repelreforged.not_found_message"), true);
            }
        }
        return super.place(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack activeItem = player.getItemInHand(usedHand);

        if(level.isClientSide) return InteractionResultHolder.pass(activeItem);
        if (RepelReforged.isRepelNearby((ServerLevel) level, player.getOnPos()))
            player.sendSystemMessage(Component.translatable(
                    "message.repelreforged.nearby_message"));
        else
            player.sendSystemMessage(Component.translatable(
                    "message.repelreforged.not_found_message"));

        return InteractionResultHolder.success(activeItem);
    }
}
