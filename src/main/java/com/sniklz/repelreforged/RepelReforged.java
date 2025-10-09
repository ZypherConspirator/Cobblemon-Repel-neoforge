package com.sniklz.repelreforged;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.spawning.context.FishingSpawningContext;
import com.sniklz.repelreforged.block.ModBlocks;
import com.sniklz.repelreforged.block.custom.RepelBlock;
import com.sniklz.repelreforged.item.ModItems;
import kotlin.Unit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Mod(RepelReforged.MODID)
public class RepelReforged {

    public static final String MODID = "repelreforged";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, MODID);

    public static final Holder<PoiType> REPEL_POI =
            POI_TYPES.register("repel", () ->
                    new PoiType(
                            Stream.of(
                                            ModBlocks.REPEL_BLOCK.get(),
                                            ModBlocks.REPEL_BLOCK_1.get(),
                                            ModBlocks.REPEL_BLOCK_2.get()
                                    ).flatMap(block -> block.getStateDefinition().getPossibleStates().stream())
                                    .collect(Collectors.toSet()), 1, 1)
            );


    public static void registerPoI(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
    }

    public static int GetMaxBlockDistanceInConfig() {
        return Math.max(Config.REPEL_RANGE_1.getAsInt(), Math.max(Config.REPEL_RANGE_2.getAsInt(), Config.REPEL_RANGE_3.getAsInt()));
    }

    public RepelReforged(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        registerPoI(modEventBus);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);


        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.HIGHEST, event -> {
            ServerLevel world = event.getCtx().getWorld();

            if (event.isCanceled() || event.getCtx() instanceof FishingSpawningContext
            ) return Unit.INSTANCE;

            BlockPos spawnPos = event.getCtx().getPosition();
            if (isRepelNearby(world, spawnPos)) {
                event.cancel();
            }

            return Unit.INSTANCE;
        });
    }

    public static boolean isRepelNearby(ServerLevel world, BlockPos pos) {

        int repelSearchDistance = GetMaxBlockDistanceInConfig();

        return world.getPoiManager().getInSquare(
                poi -> poi.is(REPEL_POI.unwrapKey().orElseThrow()),
                pos,
                repelSearchDistance,
                PoiManager.Occupancy.ANY).anyMatch(blockPos -> {
            if (world.getBlockState(blockPos.getPos()).getBlock() instanceof RepelBlock repelBlock) {
                int repelLevel = repelBlock.getBlockLevel();
                int repelRange = switch (repelLevel) {
                    case 1 -> Config.REPEL_RANGE_1.getAsInt();
                    case 2 -> Config.REPEL_RANGE_2.getAsInt();
                    case 3 -> Config.REPEL_RANGE_3.getAsInt();
                    default -> 32;
                };
                int xRange = Math.abs(blockPos.getPos().getX() - pos.getX());
                int yRange = Math.abs(blockPos.getPos().getY() - pos.getY());
                int zRange = Math.abs(blockPos.getPos().getZ() - pos.getZ());
                if (xRange < repelRange && yRange < repelRange && zRange < repelRange) {
                    return true;
                }
            }
            return false;
        });
    }

    private void commonSetup(FMLCommonSetupEvent event) {}

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModBlocks.REPEL_BLOCK);
            event.accept(ModBlocks.REPEL_BLOCK_1);
            event.accept(ModBlocks.REPEL_BLOCK_2);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}
}
