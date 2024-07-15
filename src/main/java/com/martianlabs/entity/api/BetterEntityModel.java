package com.martianlabs.entity.api;

import com.martianlabs.entity.v1_12_R1.BetterEntityType;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;

// TODO: Abilities
public interface BetterEntityModel {
    default String getDisplayNamePattern() {
        return "%name% [%health%]";
    }

    default double getMaxHealth() {
        return 20.0D;
    }

    default double getDefence() {
        return 0.0D;
    }

    default double getSpeed() {
        return 1.0D;
    }

    BetterEntityType getType();

    default boolean isBaby() {
        return false;
    }

    default BossBar getBossBar() {
        return Bukkit.createBossBar(this.getDisplayName(), BarColor.RED, BarStyle.SOLID);
    }

    default String getDisplayName() {
        return WordUtils.capitalizeFully(this.getName().replaceAll("_", " "));
    }

    String getName();

    default ItemStack getEquipment(EquipmentSlot slot) {
        return new ItemStack(Material.AIR);
    }

    default Collection<Resistance> getResistances() {
        return Collections.emptyList();
    }
}
