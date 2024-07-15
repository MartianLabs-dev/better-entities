package com.martianlabs.entity;

import com.martianlabs.config.ConfigRegistry;
import com.martianlabs.entity.api.BetterEntityModel;
import com.martianlabs.entity.api.Resistance;
import com.martianlabs.entity.v1_12_R1.BetterEntityRegistry;
import com.martianlabs.entity.v1_12_R1.BetterEntityType;
import lombok.Getter;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class BetterEntities extends JavaPlugin {
    @Getter
    private static BetterEntities instance;
    @Getter
    private static ConfigRegistry registry;

    @Override
    public void onEnable() {
        instance = this;

        // #============================================#
        // |  TESTS | NOT GOING TO BE IN FINAL VERSION  |
        // #============================================#
        // |                                            |
        // |     This made only for testing current     |
        // |   functionality and will be removed later  |
        // |                                            |
        // #============================================#

        val model = new BetterEntityModel() {
            @Override
            public String getName() {
                return "summoner";
            }

            @Override
            public BetterEntityType getType() {
                return BetterEntityType.ZOMBIE;
            }

            @Override
            public double getMaxHealth() {
                return 300.0D;
            }

            @Override
            public ItemStack getEquipment(EquipmentSlot slot) {
                if (slot == EquipmentSlot.HEAD) {
                    return new ItemStack(Material.GOLD_HELMET);
                }
                return BetterEntityModel.super.getEquipment(slot);
            }

            @Override
            public Collection<Resistance> getResistances() {
                return Arrays.asList(Resistance.ARROW, Resistance.POTION);
            }
        };

        val bandit = new BetterEntityModel() {
            @Override
            public String getName() {
                return "bandit";
            }

            @Override
            public double getMaxHealth() {
                return 120.0D;
            }

            @Override
            public BetterEntityType getType() {
                return BetterEntityType.VINDICATOR;
            }

            @Override
            public ItemStack getEquipment(EquipmentSlot slot) {
                if (slot == EquipmentSlot.HEAD) {
                    return new ItemStack(Material.GOLD_HELMET);
                }
                return BetterEntityModel.super.getEquipment(slot);
            }

            @Override
            public Collection<Resistance> getResistances() {
                return Collections.singletonList(Resistance.KNOCKBACK);
            }
        };

        BetterEntityRegistry.register(model);
        BetterEntityRegistry.register(bandit);

        registry = new ConfigRegistry();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player");
            return true;
        }

        if (!label.equals("test")) return true;

        val player = (Player) sender;

        val location = player.getLocation();

        BetterEntityRegistry.spawn(location, "summoner");
        BetterEntityRegistry.spawn(location, "bandit");

        return true;
    }


}
