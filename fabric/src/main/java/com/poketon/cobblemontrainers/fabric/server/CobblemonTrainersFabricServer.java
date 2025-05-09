package com.poketon.cobblemontrainers.fabric.server;

import com.poketon.cobblemontrainers.CobblemonTrainers;
import com.poketon.cobblemontrainers.fabric.FabricPermissionValidator;
import net.fabricmc.api.DedicatedServerModInitializer;

public class CobblemonTrainersFabricServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        CobblemonTrainers.INSTANCE.setPermissionValidator_(new FabricPermissionValidator());
    }

}
