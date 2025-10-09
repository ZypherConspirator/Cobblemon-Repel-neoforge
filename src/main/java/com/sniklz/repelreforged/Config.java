package com.sniklz.repelreforged;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue REPEL_RANGE_3 = BUILDER
            .comment("Max Repel range (in blocks)")
            .defineInRange("maximum_repel_range", 144, 0, 512);

    public static final ModConfigSpec.IntValue REPEL_RANGE_2 = BUILDER
            .comment("Super Repel range (in blocks)")
            .defineInRange("super_repel_range", 96, 0, 512);

    public static final ModConfigSpec.IntValue REPEL_RANGE_1 = BUILDER
            .comment("Repel range (in blocks)")
            .defineInRange("repel_range", 48, 0, 512);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
