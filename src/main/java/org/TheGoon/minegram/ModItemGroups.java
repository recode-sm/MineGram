package org.TheGoon.minegram;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;

public class ModItemGroups {
    public static void registerItemGroups() {
        // 재료 그룹(Materials)에 아이템 추가
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(ModItems.GRAM_BOOK);
        });
    }
}
