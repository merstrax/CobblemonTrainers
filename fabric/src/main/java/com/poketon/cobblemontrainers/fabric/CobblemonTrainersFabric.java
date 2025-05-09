package com.poketon.cobblemontrainers.fabric;

import com.poketon.cobblemontrainers.CobblemonTrainers;
import net.fabricmc.api.ModInitializer;

public class CobblemonTrainersFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CobblemonTrainers.INSTANCE.initialize();
        System.out.println("CobblemonTrainers Fabric initialized");
    }

}
