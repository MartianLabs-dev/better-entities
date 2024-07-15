package com.martianlabs.entity.api;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public interface BetterEntity extends BetterEntityModel {
    void setDisplayName(String value);

    void setDisplayNamePattern(String value);

    float getHealth();

    void setHealth(float value);

    void setMaxHealth(double value);

    void setDefence(double value);

    void setSpeed(double value);

    void setBaby(boolean value);

    void setLocation(Location location);

    void spawn(World world);

    void setEquipment(EquipmentSlot slot, ItemStack stack);

    void addResistance(Resistance value);

    void removeResistance(Resistance value);

    boolean hasResistance(Resistance value);
}
