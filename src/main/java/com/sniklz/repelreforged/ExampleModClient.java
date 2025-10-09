package com.sniklz.repelreforged;

import com.sniklz.repelreforged.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = RepelReforged.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = RepelReforged.MODID, value = Dist.CLIENT)
public class ExampleModClient {
    public ExampleModClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        RepelReforged.LOGGER.info("HELLO FROM CLIENT SETUP");
        RepelReforged.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.REPEL_BLOCK.get(), RenderType.CUTOUT);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.REPEL_BLOCK_1.get(), RenderType.CUTOUT);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.REPEL_BLOCK_2.get(), RenderType.CUTOUT);
    }
}
