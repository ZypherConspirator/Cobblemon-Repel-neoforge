package com.sniklz.repelreforged.item;

import com.sniklz.repelreforged.RepelReforged;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RepelReforged.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
