package me.andante.chord.util;

import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class CTagUtil {

    public static Boolean tagContainsItem(TagKey<Item> tag, Item item) {
        return Registry.ITEM.getOrCreateEntryList(tag).contains(Registry.ITEM.entryOf(item.getRegistryEntry().registryKey()));
    }
}
