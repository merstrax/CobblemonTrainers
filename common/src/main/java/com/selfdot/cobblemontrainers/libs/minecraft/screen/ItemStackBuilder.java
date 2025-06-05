package com.selfdot.cobblemontrainers.libs.minecraft.screen;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ItemStackBuilder {

    private final ItemStack itemStack;
    private String name = "";
    private boolean noAdditional = true;
    private final List<Text> lore = new ArrayList<>();
    private PlayerEntity playerSkull = null;

    protected ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static ItemStackBuilder itemStack(ItemStack itemStack) {
        return new ItemStackBuilder(itemStack);
    }

    public static ItemStackBuilder itemStack(Item item) {
        return new ItemStackBuilder(new ItemStack(item));
    }

    public static ItemStackBuilder skullOf(PlayerEntity player) {
        ItemStackBuilder itemStackBuilder = itemStack(Items.PLAYER_HEAD);
        itemStackBuilder.playerSkull = player;
        return itemStackBuilder;
    }

    public ItemStackBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ItemStackBuilder withAdditional() {
        this.noAdditional = false;
        return this;
    }

    public ItemStackBuilder withLore(Text loreLine) {
        lore.add(loreLine);
        return this;
    }

    public ItemStack build() {
        if (!name.isEmpty()) {
            itemStack.set(DataComponentTypes.ITEM_NAME, Text.literal(name.replace("\"", "\\\"")).setStyle(Style.EMPTY.withItalic(true)));
        }
        if (noAdditional) {
            itemStack.set(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        }
        if (!lore.isEmpty()) {
            itemStack.set(DataComponentTypes.LORE, new LoreComponent(lore));
        }

        return itemStack;
    }
}
