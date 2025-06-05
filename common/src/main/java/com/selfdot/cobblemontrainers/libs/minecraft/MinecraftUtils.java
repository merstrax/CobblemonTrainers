package com.selfdot.cobblemontrainers.libs.minecraft;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraft.util.Formatting.*;

public class MinecraftUtils {

    public static void spawnFireworkExplosion(
        Vec3d position,
        World world,
        DyeColor primaryColor,
        DyeColor fadeColor
    ) {

    }

    public static String colourize(String string) {
        return string
            .replace("&0", String.valueOf(BLACK)).replace("&1", String.valueOf(DARK_BLUE))
            .replace("&2", String.valueOf(DARK_GREEN)).replace("&3", String.valueOf(DARK_AQUA))
            .replace("&4", String.valueOf(DARK_RED)).replace("&5", String.valueOf(DARK_PURPLE))
            .replace("&6", String.valueOf(GOLD)).replace("&7", String.valueOf(GRAY))
            .replace("&8", String.valueOf(DARK_GRAY)).replace("&9", String.valueOf(BLUE))
            .replace("&a", String.valueOf(GREEN)).replace("&b", String.valueOf(AQUA))
            .replace("&c", String.valueOf(RED)).replace("&d", String.valueOf(LIGHT_PURPLE))
            .replace("&e", String.valueOf(YELLOW)).replace("&f", String.valueOf(WHITE))
            .replace("&k", String.valueOf(OBFUSCATED)).replace("&l", String.valueOf(BOLD))
            .replace("&m", String.valueOf(STRIKETHROUGH)).replace("&n", String.valueOf(UNDERLINE))
            .replace("&o", String.valueOf(ITALIC)).replace("&r", String.valueOf(RESET))
            .replace("&A", String.valueOf(GREEN)).replace("&B", String.valueOf(AQUA))
            .replace("&C", String.valueOf(RED)).replace("&D", String.valueOf(LIGHT_PURPLE))
            .replace("&E", String.valueOf(YELLOW)).replace("&F", String.valueOf(WHITE))
            .replace("&K", String.valueOf(OBFUSCATED)).replace("&L", String.valueOf(BOLD))
            .replace("&M", String.valueOf(STRIKETHROUGH)).replace("&N", String.valueOf(UNDERLINE))
            .replace("&O", String.valueOf(ITALIC)).replace("&R", String.valueOf(RESET));

    }

}
